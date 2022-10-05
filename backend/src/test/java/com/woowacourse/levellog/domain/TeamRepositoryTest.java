package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamRepository의")
class TeamRepositoryTest extends RepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

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

    @Nested
    @DisplayName("getTeamWithParticipants 메서드는")
    class GetTeamWithParticipants {

        @Test
        @DisplayName("teamId에 해당하는 레코드가 존재하면 Participants 엔티티를 담은 Team 엔티티를 반환한다.")
        void success() {
            // given
            final Member to = saveMember("릭");
            final Member from = saveMember("로마");
            final Long saveTeamId = saveTeam(to, from)
                    .getId();

            entityManager.clear();

            // when
            final Team actual = teamRepository.getTeam(saveTeamId);

            // then
            assertThat(actual.getId()).isEqualTo(saveTeamId);
            assertThat(actual.getParticipants().getValues()).isNotEmpty();
        }

        @Test
        @DisplayName("teamId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getTeamWithParticipants_notExist_exception() {
            // given
            final Long teamId = 999L;

            // when & then
            assertThatThrownBy(() -> teamRepository.getTeamWithParticipants(teamId))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }
}
