package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("PreQuestionController의")
public class PreQuestionControllerTest extends ControllerTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 null이 들어오면 예외를 던진다.")
        void preQuestionNull_Exception(final String preQuestion) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionCreateDto preQuestionCreateDto = new PreQuestionCreateDto(preQuestion);
            final String requestContent = objectMapper.writeValueAsString(preQuestionCreateDto);

            doThrow(new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다."))
                    .when(preQuestionService)
                    .save(preQuestionCreateDto, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/1/pre-questions")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("preQuestion must not be blank");

            // docs
            perform.andDo(document("pre-question/create/exception/null-and-blank"));
        }

    }
}
