package com.woowacourse.levellog.domain;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.BEFORE_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Team의")
class TeamTest {

    public static Team saveTeam() {
        final TeamDetail teamDetail = new TeamDetail("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", TEAM_START_TIME, "profileUrl", 2);

        return new Team(teamDetail, 1L, List.of(1L, 2L, 3L), List.of(4L, 5L, 6L));
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("팀 이름, 장소, 시작 시간, 참가자를 수정한다.")
        void success() {
            // given
            final Team team = saveTeam();
            final TeamDetail updatedTeam = new TeamDetail("브라운과 카페 투어", "잠실 어드레스룸", TEAM_START_TIME, "profile.img", 2);

            // when
            team.update(updatedTeam, 1L, List.of(1L, 2L, 3L), List.of(14L, 15L, 16L), BEFORE_START_TIME);

            // then
            assertAll(
                    () -> assertThat(team).extracting("detail")
                            .extracting("title", "place", "startAt", "profileUrl", "interviewerNumber")
                            .containsExactly("브라운과 카페 투어", "잠실 어드레스룸", TEAM_START_TIME, "profile.img", 2),
                    () -> assertThat(team.getParticipants().getValues())
                            .extracting("memberId")
                            .contains(1L, 2L, 3L, 14L, 15L, 16L)
            );
        }

        @Test
        @DisplayName("인터뷰 시작 이후인 경우 예외를 던진다.")
        void update_updateAfterStartAt_exception() {
            // given
            final Team team = saveTeam();
            final TeamDetail updatedTeam = new TeamDetail("브라운과 카페 투어", "잠실 어드레스룸", TEAM_START_TIME, "profile.img", 2);

            // when & then
            assertThatThrownBy(
                    () -> team.update(updatedTeam, 1L, List.of(1L, 2L, 3L), List.of(14L, 15L, 16L), AFTER_START_TIME))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("close 메소드는")
    class Close {

        @Test
        @DisplayName("팀 인터뷰를 종료 상태로 바꾼다.")
        void success() {
            // given
            final Team team = saveTeam();

            // when
            team.close(AFTER_START_TIME);

            // then
            assertThat(team.isClosed()).isTrue();
        }

        @Test
        @DisplayName("이미 종료된 인터뷰를 종료하려는 경우 예외가 발생한다.")
        void close_alreadyClosed_exception() {
            // given
            final Team team = saveTeam();

            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> team.close(AFTER_START_TIME))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간 전 종료를 요청할 경우 예외가 발생한다.")
        void close_notInProgress_exception() {
            // given
            final Team team = saveTeam();

            // when & then
            assertThatThrownBy(() -> team.close(BEFORE_START_TIME))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContaining("인터뷰 진행중인 상태가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("팀을 deleted 상태로 만든다.")
        void success() {
            // given
            final Team team = saveTeam();

            // when
            team.delete(BEFORE_START_TIME);

            // then
            assertThat(team.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 삭제를 요청할 경우 예외가 발생한다.")
        void delete_notReady_exception() {
            // given
            final Team team = saveTeam();

            // when & then
            assertThatThrownBy(() -> team.delete(AFTER_START_TIME))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("status 메서드는")
    class Status {

        @Test
        @DisplayName("현재 시간이 인터뷰 시작 시간보다 이전인 경우 READY를 반환한다.")
        void status_ready_success() {
            // given
            final Team team = saveTeam();

            // when
            final TeamStatus actual = team.status(BEFORE_START_TIME);

            // then
            assertThat(actual).isEqualTo(TeamStatus.READY);
        }

        @Test
        @DisplayName("인터뷰 시작시간이 현재 시간보다 이후이면서 종료되지 않은 경우 IN_PROGRESS를 반환한다.")
        void status_inProgress_success() {
            // given
            final Team team = saveTeam();

            // when
            final TeamStatus actual = team.status(AFTER_START_TIME);

            // then
            assertThat(actual).isEqualTo(TeamStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("인터뷰가 종료된 경우 CLOSED를 반환한다.")
        void status_closed_success() {
            // given
            final Team team = saveTeam();
            team.close(AFTER_START_TIME);

            // when
            final TeamStatus actual = team.status(AFTER_START_TIME);

            // then
            assertThat(actual).isEqualTo(TeamStatus.CLOSED);
        }
    }
}
