package com.woowacourse.levellog.authentication.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("OAuthController의")
class OAuthControllerTest extends ControllerTest {

    @ParameterizedTest
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    @DisplayName("login 메서드는 코드에 공백이나 null이 들어오면 예외를 던진다.")
    void login(final String code) throws Exception {
        // given
        final GithubCodeRequest request = new GithubCodeRequest(code);
        final String requestContent = objectMapper.writeValueAsString(request);

        // when
        final ResultActions perform = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andDo(print());

        // then
        perform.andExpect(status().isBadRequest());
    }
}
