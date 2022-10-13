package com.woowacourse.levellog.domain;

import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("TeamDetail의")
class TeamDetailTest {

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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, startAt, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, startAt, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 프로필 사진이 null 또는 공백입니다.");
        }

        @Test
        @DisplayName("팀 프로필 사진 url이 2148자를 초과할 경우 예외를 던진다.")
        void constructor_profileUrlInvalidLength_exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "a".repeat(2149);

            // when & then
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 1))
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
            assertThatThrownBy(() -> new TeamDetail(title, place, TEAM_START_TIME, profileUrl, 0))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 생성시 인터뷰어 수는 1명 이상이어야 합니다");
        }
    }
}
