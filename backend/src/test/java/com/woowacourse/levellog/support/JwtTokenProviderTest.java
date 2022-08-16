package com.woowacourse.levellog.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.authentication.exception.InvalidTokenException;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("JwtTokenProvider의")
class JwtTokenProviderTest {

    private static final String SECRET_KEY = "fe164d0918ae5aa4b159c57f2916fe37cbfc2a3282bc8da7f79b67c06730370115fe172bdec88d81d369790416ccf3c2044d6a3ebbb2e92a93225b026dae6c36";
    private static final int EXPIRE_LENGTH = 36000000;

    @Test
    @DisplayName("createToken 메서드는 토큰을 생성한다.")
    void createToken() {
        // given
        final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXPIRE_LENGTH);

        // when
        final String token = jwtTokenProvider.createToken("1234567");

        // then
        assertThat(token).isNotNull();
    }

    @Nested
    @DisplayName("getPayload 메서드는")
    class GetPayload {

        @Test
        @DisplayName("페이로드를 반환한다.")
        void success() {
            // given
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXPIRE_LENGTH);
            final String token = jwtTokenProvider.createToken("1234567");

            // when
            final String actual = jwtTokenProvider.getPayload(token);

            // then
            assertThat(actual).isEqualTo("1234567");
        }

        @Test
        @DisplayName("유효기간이 만료된 토큰이면 예외를 던진다.")
        void getPayload_expiredToken_exception() {
            // given
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 0);
            final String token = jwtTokenProvider.createToken("1234567");

            // when & then
            assertThatThrownBy(() -> jwtTokenProvider.getPayload(token))
                    .isInstanceOf(InvalidTokenException.class);
        }

        @Test
        @DisplayName("페이로드에 정수가 아닌 값이 들어있으면 예외를 던진다.")
        void getPayload_notIntPayload_exception() {
            // given
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, EXPIRE_LENGTH);
            final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJoaWhpIn0.SH1aNhtKVDIbM6uuzx5wzP7i0cMeg-VLsEwyEf7DbBA";

            // when & then
            assertThatThrownBy(() -> jwtTokenProvider.getPayload(token))
                    .isInstanceOf(InvalidTokenException.class);
        }
    }
}
