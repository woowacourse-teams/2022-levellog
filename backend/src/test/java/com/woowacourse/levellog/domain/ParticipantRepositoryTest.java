package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ParticipantRepository의")
class ParticipantRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByTeam은 team이 일치하는 모든 participant를 반환한다.")
    void findByTeam() {
        // given
        final Member alien = saveMember("알린");
        final Member roma = saveMember("로마");

        final Team team = saveTeam(alien, roma);

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
        final Member alien = saveMember("알린");
        final Member roma = saveMember("로마");

        final Team team = saveTeam(alien, roma);

        // when
        participantRepository.deleteByTeam(team);

        // then
        assertThat(participantRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("existsByMemberAndTeam는 멤버와 팀이 일치 여부를 반환한다.")
    void existsByMemberAndTeam() {
        // given
        final Member alien = saveMember("알린");
        final Member roma = saveMember("로마");

        final Team team = saveTeam(alien);

        // when
        final boolean existsAlien = participantRepository.existsByMemberIdAndTeam(alien.getId(), team);
        final boolean existsRoma = participantRepository.existsByMemberIdAndTeam(roma.getId(), team);

        // then
        assertAll(
                () -> assertThat(existsAlien).isTrue(),
                () -> assertThat(existsRoma).isFalse()
        );
    }

    @Test
    @DisplayName("findAllByMember 메서드는 해당 멤버가 포함된 모든 participant 반환한다.")
    void findAllByMember() {
        // given
        final Member alien = saveMember("알린");
        final Member roma = saveMember("로마");

        final Team team1 = saveTeam(alien, roma);
        final Team team2 = saveTeam(roma);

        // when
        final List<Participant> participants = participantRepository.findAllByMember(roma);

        final List<Long> actualTeamIds = toTeamIds(participants);

        // then
        assertAll(
                () -> assertThat(participants).hasSize(2),
                () -> assertThat(actualTeamIds).contains(team1.getId(), team2.getId())
        );
    }

    private List<Long> toTeamIds(final List<Participant> participants) {
        return participants.stream()
                .map(Participant::getTeam)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }
}
