package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("ParticipantRepository의")
public class ParticipantRepositoryTest {

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("findByTeam은 team이 일치하는 모든 participant를 반환한다.")
    void findByTeam() {
        // given
        final Team team = teamRepository.save(new Team(
                "네오와 함께하는 레벨 인터뷰",
                "선릉 트랙룸",
                LocalDateTime.now().plusDays(3),
                "profile.img"));

        final Member alien = memberRepository.save(new Member("알린", 12345678, "alien.img"));
        final Member roma = memberRepository.save(new Member("로마", 56781234, "roma.img"));

        participantRepository.save(new Participant(team, alien, true));
        participantRepository.save(new Participant(team, roma, false));

        // when
        final List<Participant> participants = participantRepository.findByTeam(team);

        // then
        assertAll(
                () -> assertThat(participants).hasSize(2),
                () -> assertThat(participants.get(0).getMember()).isEqualTo(alien),
                () -> assertThat(participants.get(1).getMember()).isEqualTo(roma)
        );
    }

    @Test
    @DisplayName("deleteByTeam는 team이 일치하는 모든 participant를 제거한다.")
    void deleteByTeam() {
        // given
        final Team team = teamRepository.save(new Team(
                "네오와 함께하는 레벨 인터뷰",
                "선릉 트랙룸",
                LocalDateTime.now().plusDays(3),
                "profile.img"));

        final Member alien = memberRepository.save(new Member("알린", 12345678, "alien.img"));
        final Member roma = memberRepository.save(new Member("로마", 56781234, "roma.img"));

        participantRepository.save(new Participant(team, alien, true));
        participantRepository.save(new Participant(team, roma, false));

        // when
        participantRepository.deleteByTeam(team);

        // then
        assertThat(participantRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("existsByMemberAndTeam는 멤버와 팀이 일치 여부를 반환한다.")
    void existsByMemberAndTeam() {
        // given
        final Team team = teamRepository.save(new Team(
                "네오와 함께하는 레벨 인터뷰",
                "선릉 트랙룸",
                LocalDateTime.now().plusDays(3),
                "profile.img"));

        final Member alien = memberRepository.save(new Member("알린", 12345678, "alien.img"));
        final Member roma = memberRepository.save(new Member("로마", 56781234, "roma.img"));

        participantRepository.save(new Participant(team, alien, true));

        // when
        final boolean existsAlien = participantRepository.existsByMemberAndTeam(alien, team);
        final boolean existsRoma = participantRepository.existsByMemberAndTeam(roma, team);

        // then
        assertAll(
                () -> assertThat(existsAlien).isTrue(),
                () -> assertThat(existsRoma).isFalse()
        );
    }

    @Test
    @DisplayName("findAllByMember 메서드는 해당 멤버가 포함된 모든 partiipant를 반환한다.")
    void findAllByMember() {
        // given
        final Team team = teamRepository.save(
                new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", LocalDateTime.now().plusDays(3), "profile.img"));
        final Team team2 = teamRepository.save(
                new Team("브라운 잠실", "잠실 트랙룸", LocalDateTime.now().plusDays(3), "profile.img"));

        final Member alien = memberRepository.save(new Member("알린", 12345678, "alien.img"));
        final Member roma = memberRepository.save(new Member("로마", 56781234, "roma.img"));

        final Participant romaParticipant1 = participantRepository.save(new Participant(team, roma, false));
        final Participant romaParticipant2 = participantRepository.save(new Participant(team2, roma, true));

        participantRepository.save(new Participant(team, alien, true)); // roma 비포함 participant

        // when
        final List<Participant> participants = participantRepository.findAllByMember(roma);

        // then
        assertAll(
                () -> assertThat(participants).hasSize(2),
                () -> assertThat(participants).contains(romaParticipant1, romaParticipant2)
        );
    }
}
