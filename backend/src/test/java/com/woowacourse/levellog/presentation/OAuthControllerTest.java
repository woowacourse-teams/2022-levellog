package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("OAuthController의")
class OAuthControllerTest extends ControllerTest {

    @Nested
    @DisplayName("login 메서드는")
    class Login {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("코드에 공백이나 null이 들어오면 예외를 던진다.")
        void login_nullAndEmptyCode_exceptionThrown(final String code) throws Exception {
            // given
            final GithubCodeDto request = new GithubCodeDto(code);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andDo(document("auth/login/null"));
        }

        @Test
        @DisplayName("깃허브에 accessToken 요청에 실패하면 예외를 던진다.")
        void login_failToGetAccessToken_exceptionThrown() throws Exception {
            // given
            final GithubCodeDto request = new GithubCodeDto(VALID_TOKEN);
            final String requestContent = objectMapper.writeValueAsString(request);

            given(oAuthService.login(request))
                    .willThrow(new IllegalStateException("Github 로그인 요청 실패 - authorizationCode:" + VALID_TOKEN));

            // when
            final ResultActions perform = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isInternalServerError())
                    .andDo(document("auth/login/github-fail"));
        }
    }
}
