package com.woowacourse.levellog.authentication.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OAuthController.class)
@DisplayName("OAuthController의")
class OAuthControllerTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private OAuthService oAuthService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
