package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
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
                () -> assertThat(participants.get(0).getMemberId()).isEqualTo(alien.getId()),
                () -> assertThat(participants.get(1).getMemberId()).isEqualTo(roma.getId())
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
    @DisplayName("existsByMemberIdAndTeam는 멤버와 팀이 일치 여부를 반환한다.")
    void existsByMemberIdAndTeam() {
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
}
