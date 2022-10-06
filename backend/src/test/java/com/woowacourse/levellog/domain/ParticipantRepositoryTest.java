package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ParticipantRepository의")
class ParticipantRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("existsByMemberIdAndTeam는 멤버와 팀이 일치 여부를 반환한다.")
    void existsByMemberIdAndTeam() {
        // given
        final Member alien = saveMember("알린");
        final Member pepper = saveMember("페퍼");

        final Member roma = saveMember("로마");

        final Team team = saveTeam(alien, pepper);

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
