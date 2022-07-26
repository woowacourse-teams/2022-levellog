package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Member 의")
class MemberTest {

    @Nested
    @DisplayName("updateNickname 메서드는")
    class updateNickname {

        @Test
        @DisplayName("닉네임을 변경한다.")
        void updateNickname() {
            // given
            final Member member = new Member("로마", 123456, "image.png");

            // when
            member.updateNickname("알린");

            // then
            assertThat(member.getNickname()).isEqualTo("알린");
        }

        @Test
        @DisplayName("닉네임이 50자를 초과할 경우 예외를 던진다.")
        void nicknameInvalidLength_Exception() {
            // given
            final Member member = new Member("로마", 123456, "image.png");
            final String invalidNickname = "a".repeat(51);

            // when & then
            assertThatThrownBy(() -> member.updateNickname(invalidNickname)).isInstanceOf(InvalidFieldException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("닉네임이 공백이나 null일 경우 예외를 던진다.")
        void nicknameBlank_Exception(final String invalidNickname) {
            // given
            final Member member = new Member("로마", 123456, "image.png");

            // when & then
            assertThatThrownBy(() -> member.updateNickname(invalidNickname)).isInstanceOf(InvalidFieldException.class);
        }
    }
}
