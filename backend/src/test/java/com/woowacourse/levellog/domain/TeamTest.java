package com.woowacourse.levellog.domain;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.BEFORE_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Team의")
class TeamTest {

    private Team saveTeam() {
        return saveTeam(2);
    }

    private Team saveTeam(final int interviewerNumber) {
        return new Team("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", TEAM_START_TIME, "profileUrl", interviewerNumber);
    }

    @Nested
    @DisplayName("생성자 메서드는")
    class Constructor {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 이름이 null 또는 공백이 들어오면 예외를 던진다.")
        void constructor_titleNullOrBlank_exception(final String title) {
            // given
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 이름이 255자를 초과할 경우 예외를 던진다.")
        void constructor_titleInvalidLength_exception() {
            // given
            final String title = "a".repeat(256);
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름은 255 이하여야 합니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 장소가 null 또는 공백이 들어오면 예외를 던진다.")
        void constructor_placeNullOrBlank_exception(final String place) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 장소가 255자를 초과할 경우 예외를 던진다.")
        void constructor_placeInvalidLength_exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "a".repeat(256);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소 이름은 255 이하여야 합니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("인터뷰 시작 시간이 null이 들어오면 예외를 던진다.")
        void constructor_startAtNull_exception(final LocalDateTime startAt) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("시작 시간이 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 과거인 경우 예외를 던진다.")
        void constructor_startAtInvalidDateTime_exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("인터뷰 시작 시간은 현재 시간 이후여야 합니다.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 프로필 사진 url이 null 또는 공백이 들어오면 예외를 던진다.")
        void constructor_profileUrlNullOrBlank_exception(final String profileUrl) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 프로필 사진이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 프로필 사진 url이 2048자를 초과할 경우 예외를 던진다.")
        void constructor_profileUrlInvalidLength_exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "a".repeat(2049);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 프로필 사진을 입력했습니다.");
        }

        @Test
        @DisplayName("인터뷰어 수가 1명 미만이면 예외를 던진다.")
        void constructor_minInterviewerNumber_exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.org";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, TEAM_START_TIME, profileUrl, 0))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 생성시 인터뷰어 수는 1명 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("팀 이름, 장소, 시작 시간을 수정한다.")
        void success() {
            // given
            final Team team = saveTeam();
            final Team updatedTeam = new Team("브라운과 카페 투어", "잠실 어드레스룸", TEAM_START_TIME, "profile.img", 2);

            // when
            team.update(updatedTeam, BEFORE_START_TIME);

            // then
            assertAll(
                    () -> assertThat(team.getTitle()).isEqualTo(updatedTeam.getTitle()),
                    () -> assertThat(team.getPlace()).isEqualTo(updatedTeam.getPlace()),
                    () -> assertThat(team.getStartAt()).isEqualTo(updatedTeam.getStartAt()),
                    () -> assertThat(team.getInterviewerNumber()).isEqualTo(updatedTeam.getInterviewerNumber())
            );
        }

        @Test
        @DisplayName("인터뷰 시작 이후인 경우 예외를 던진다.")
        void update_updateAfterStartAt_exception() {
            // given
            final Team team = saveTeam();
            final Team updatedTeam = new Team("브라운과 카페 투어", "잠실 어드레스룸", TEAM_START_TIME, "profile.img", 2);

            // when & then
            assertThatThrownBy(() -> team.update(updatedTeam, AFTER_START_TIME))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("validParticipantNumber 메서드는")
    class ValidParticipantNumber {

        @ParameterizedTest
        @CsvSource(value = {"1,1", "2,1", "2,2"})
        @DisplayName("isValidParticipantNumber 메서드는 인터뷰어 수를 참고해서 참가자 수가 유효한지 계산한다.")
        void validParticipantNumber_ifNotValid_exception(final int interviewerNumber, final int participantNumber) {
            // given
            final Team team = saveTeam(interviewerNumber);

            // when & then
            assertThatThrownBy(() -> team.validParticipantNumber(participantNumber))
                    .isInstanceOf(InvalidFieldException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {"1,2", "2,3"})
        @DisplayName("isValidParticipantNumber 메서드는 인터뷰어 수를 참고해서 참가자 수가 유효한지 계산한다.")
        void validParticipantNumber_ifValid_success(final int interviewerNumber, final int participantNumber) {
            // given
            final Team team = saveTeam(interviewerNumber);

            // when & then
            assertThatCode(() -> team.validParticipantNumber(participantNumber))
                    .doesNotThrowAnyException();
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
    @DisplayName("validateInProgress 메서드는")
    class ValidateInProgress {

        @Test
        @DisplayName("InProgress 상태가 아닐 때 예외가 발생한다.")
        void validate_notInProgress_exception() {
            // given
            final Team team = saveTeam();

            // when & then
            assertThatThrownBy(() -> team.validateInProgress(BEFORE_START_TIME))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContaining("인터뷰 진행중인 상태가 아닙니다.");
        }

        @Test
        @DisplayName("팀 인터뷰가 이미 종료된 상태면 예외를 발생시킨다.")
        void validate_beforeClose_exception() {
            // given
            final Team team = saveTeam();

            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> team.validateInProgress(AFTER_START_TIME.plusDays(1)))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }
    }

    @Nested
    @DisplayName("validateReady 메서드는")
    class ValidateReady {

        @Test
        @DisplayName("입력 받은 시간이 인터뷰 시작 시간보다 이후면 예외가 발생한다.")
        void validateReady_notReady_exception() {
            // given
            final Team team = saveTeam();

            // when & then
            assertThatThrownBy(
                    () -> team.validateReady(AFTER_START_TIME))
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
