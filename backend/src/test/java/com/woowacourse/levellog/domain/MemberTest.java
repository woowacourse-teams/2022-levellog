package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Member 의")
class MemberTest {

    @Nested
    @DisplayName("생성자는")
    class Constructor {

        @Test
        @DisplayName("닉네임이 50자를 초과할 경우 예외를 던진다.")
        void constructor_nicknameInvalidLength_exception() {
            // given
            final String invalidNickname = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> new Member(invalidNickname, 123456, "validProfileUrl"))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("닉네임은 50자 이하여야합니다.", String.valueOf(invalidNickname.length()));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("닉네임이 공백이나 null일 경우 예외를 던진다.")
        void constructor_nicknameBlank_exception(final String invalidNickname) {
            // when & then
            assertThatThrownBy(() -> new Member(invalidNickname, 123456, "validProfileUrl"))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("닉네임은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("githubId가 null일 경우 예외를 던진다.")
        void constructor_githubIdNull_exception() {
            // when & then
            assertThatThrownBy(() -> new Member("test", null, "validProfileUrl"))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("깃허브 ID는 null일 수 없습니다.");
        }

        @Test
        @DisplayName("프로필 url은 2048자를 초과할 경우 예외를 던진다.")
        void constructor_profileUrlInvalidLength_exception() {
            // given
            final String invalidProfileUrl = "a".repeat(2049);

            // when & then
            assertThatThrownBy(() -> new Member("test", 123456, invalidProfileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("프로필 url은 2048자 이하여야합니다.", String.valueOf(invalidProfileUrl.length()));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("프로필 url이 공백이나 null일 경우 예외를 던진다.")
        void constructor_profileUrlBlank_exception(final String invalidProfileUrl) {
            // when & then
            assertThatThrownBy(() -> new Member("test", 123456, invalidProfileUrl))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("프로필 url은 공백이나 null일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("updateNickname 메서드는")
    class UpdateNickname {

        @Test
        @DisplayName("닉네임을 변경한다.")
        void success() {
            // given
            final Member member = new Member("로마", 123456, "image.png");

            // when
            member.updateNickname("알린");

            // then
            assertThat(member.getNickname()).isEqualTo("알린");
        }

        @Test
        @DisplayName("닉네임이 50자를 초과할 경우 예외를 던진다.")
        void updateNickname_nicknameInvalidLength_exception() {
            // given
            final Member member = new Member("로마", 123456, "image.png");
            final String invalidNickname = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> member.updateNickname(invalidNickname))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("닉네임은 50자 이하여야합니다.", String.valueOf(invalidNickname.length()));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("닉네임이 공백이나 null일 경우 예외를 던진다.")
        void updateNickname_nicknameBlank_exception(final String invalidNickname) {
            // given
            final Member member = new Member("로마", 123456, "image.png");

            // when & then
            assertThatThrownBy(() -> member.updateNickname(invalidNickname))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("닉네임은 공백이나 null일 수 없습니다.");
        }
    }
}
