package com.woowacourse.levellog.presentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.admin.dto.AdminPasswordDto;
import com.woowacourse.levellog.admin.exception.WrongPasswordException;
import com.woowacourse.levellog.common.support.DebugMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("AdminController의")
class AdminControllerTest extends ControllerTest {

    @Nested
    @DisplayName("login 메서드는")
    class Login {

        private static final String BASE_SNIPPET_PATH = "admin/login/exception/";

        @Test
        @DisplayName("비밀번호가 틀리면 예외를 던진다.")
        void login_wrongPassword_exception() throws Exception {
            // given
            final AdminPasswordDto request = new AdminPasswordDto("levellog1!");
            final String message = "비밀번호가 틀렸습니다.";

            BDDMockito.willThrow(new WrongPasswordException(DebugMessage.init()))
                    .given(adminService)
                    .login(request);

            // when
            final ResultActions perform = requestLogin(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "wrong-password"));
        }

        private ResultActions requestLogin(final AdminPasswordDto request) throws Exception {
            return requestPost("/admin/login", request);
        }
    }
}
