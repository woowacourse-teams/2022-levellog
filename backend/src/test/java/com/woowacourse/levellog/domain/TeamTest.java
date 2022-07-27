package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

@DisplayName("Team의")
class TeamTest {

    @Nested
    @DisplayName("생성자 메서드는")
    class constructor {

        @ParameterizedTest
        @NullSource
        @DisplayName("팀 이름이 null이 들어오면 예외를 던진다.")
        void titleNull_Exception(final String title) {
            // given
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 없습니다.");
        }

        @ParameterizedTest
        @EmptySource
        @DisplayName("팀 이름이 공백이 들어오면 예외를 던진다.")
        void titleEmpty_Exception(final String title) {
            // given
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 비어있습니다.");
        }

        @Test
        @DisplayName("팀 이름이 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() {
            // given
            final String title = "a".repeat(256);
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 이름을 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("팀 장소가 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String place) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 없습니다.");
        }

        @ParameterizedTest
        @EmptySource
        @DisplayName("팀 장소가 공백이 들어오면 예외를 던진다.")
        void placeEmpty_Exception(final String place) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 비어있습니다.");
        }

        @Test
        @DisplayName("팀 장소가 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "a".repeat(256);
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 장소를 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("인터뷰 시작 시간이 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception(final LocalDateTime startAt) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("시작 시간이 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 과거인 경우 예외를 던진다.")
        void startAtInvalidDateTime_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final String profileUrl = "profile.img";

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 시작 시간을 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("팀 프로필 사진 url이 null이 들어오면 예외를 던진다.")
        void profileUrlNull_Exception(final String profileUrl) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 프로필 사진이 없습니다.");
        }

        @ParameterizedTest
        @EmptySource
        @DisplayName("팀 프로필 사진 url이 공백이 들어오면 예외를 던진다.")
        void profileUrlEmpty_Exception(final String profileUrl) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 프로필 사진이 비어있습니다.");
        }

        @Test
        @DisplayName("팀 프로필 사진 url이 2048자를 초과할 경우 예외를 던진다.")
        void profileUrlInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "a".repeat(2049);

            // when & then
            assertThatThrownBy(() -> new Team(title, place, startAt, profileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 프로필 사진을 입력했습니다.");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("팀 이름, 장소, 시작 시간을 수정한다.")
        void update() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final String updatePlace = "잠실 어드레스룸";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when
            team.update(updateTitle, updatePlace, updateStartAt);

            // then
            assertAll(
                    () -> assertThat(team.getTitle()).isEqualTo(updateTitle),
                    () -> assertThat(team.getPlace()).isEqualTo(updatePlace),
                    () -> assertThat(team.getStartAt()).isEqualTo(updateStartAt)
            );
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("팀 이름이 null이 들어오면 예외를 던진다.")
        void titleNull_Exception(final String updateTitle) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updatePlace = "잠실 어드레스룸";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 없습니다.");
        }

        @ParameterizedTest
        @EmptySource
        @DisplayName("팀 이름이 공백이 들어오면 예외를 던진다.")
        void titleEmpty_Exception(final String updateTitle) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updatePlace = "잠실 어드레스룸";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("팀 이름이 비어있습니다.");
        }

        @Test
        @DisplayName("팀 이름이 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "a".repeat(256);
            final String updatePlace = "잠실 어드레스룸";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 팀 이름을 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("팀 장소가 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String updatePlace) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 없습니다.");
        }

        @ParameterizedTest
        @EmptySource
        @DisplayName("팀 장소가 공백이 들어오면 예외를 던진다.")
        void placeEmpty_Exception(final String updatePlace) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("장소가 비어있습니다.");
        }

        @Test
        @DisplayName("팀 장소가 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final String updatePlace = "a".repeat(256);
            final LocalDateTime updateStartAt = LocalDateTime.now().plusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 장소를 입력했습니다.");
        }

        @ParameterizedTest
        @NullSource
        @DisplayName("인터뷰 시작 시간이 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception(final LocalDateTime updateStartAt) {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final String updatePlace = "잠실 어드레스룸";

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("시작 시간이 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 과거인 경우 예외를 던진다.")
        void startAtInvalidDateTime_Exception() {
            // given
            final String title = "네오와 함께하는 레벨 인터뷰";
            final String place = "선릉 트랙룸";
            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final String profileUrl = "profile.img";

            final Team team = new Team(title, place, startAt, profileUrl);
            final String updateTitle = "브라운과 카페 투어";
            final String updatePlace = "잠실 어드레스룸";
            final LocalDateTime updateStartAt = LocalDateTime.now().minusDays(10);

            // when & then
            assertThatThrownBy(() -> team.update(updateTitle, updatePlace, updateStartAt))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("잘못된 시작 시간을 입력했습니다.");
        }
    }
}