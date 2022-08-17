package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.PasswordDto;
import com.woowacourse.levellog.admin.exception.WrongPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AdminService의")
public class AdminServiceTest extends ServiceTest {

    @Nested
    @DisplayName("login 메서드는")
    class Login {

        @Test
        @DisplayName("암호화된 비밀번호와 일치하는 비밀번호를 입력하면 token을 발급한다.")
        void success() {
            // given
            final PasswordDto request = new PasswordDto("levellog1!");

            // when
            final AdminAccessTokenDto actual = adminService.login(request);

            // then
            final String token = actual.getAccessToken();
            final String payload = jwtTokenProvider.getPayload(token);

            assertThat(payload).isEqualTo("This is admin token.");
        }

        @Test
        @DisplayName("잘못된 비밀번호를 입력하면 예외를 던진다.")
        void login_wrongPassword_exception() {
            // given
            final PasswordDto request = new PasswordDto("wrong-password");

            // when & then
            assertThatThrownBy(() -> adminService.login(request))
                    .isInstanceOf(WrongPasswordException.class);
        }
    }
}
