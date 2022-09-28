package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamRepository의")
class TeamRepositoryTest extends RepositoryTest {

    @Nested
    @DisplayName("getTeam 메서드는")
    class GetTeam {

        @Test
        @DisplayName("teamId에 해당하는 레코드가 존재하면 id에 해당하는 Team 엔티티를 반환한다.")
        void success() {
            // given
            final Member to = saveMember("릭");
            final Member from = saveMember("로마");
            final Long expected = saveTeam(to, from)
                    .getId();

            // when
            final Long actual = teamRepository.getTeam(expected)
                    .getId();

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("teamId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getTeam_notExist_exception() {
            // given
            final Long teamId = 999L;

            // when & then
            assertThatThrownBy(() -> teamRepository.getTeam(teamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }
}
