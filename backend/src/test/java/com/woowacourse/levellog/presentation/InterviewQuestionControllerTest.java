package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.interview_question.dto.InterviewQuestionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("InterviewQuestionController 클래스의")
class InterviewQuestionControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class SaveTest {

        private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.Y5wT9jBcP1lvMtjRqxaF0gMNDlgY5xs8SPhBKYChRn8";

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void save_contentBlank_exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            final InterviewQuestionDto request = InterviewQuestionDto.from(" ");
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/interview-questions", 1)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("interview-question/save/exception-contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void save_interviewQuestionInvalidLength_Exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?".repeat(255));
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InvalidFieldException("인터뷰 질문은 255자 이하여야합니다."))
                    .when(interviewQuestionService)
                    .save(request, 1L, 1L);


            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/interview-questions", 1)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 질문은 255자 이하여야합니다."));

            // docs
            perform.andDo(document("interview-question/save/exception-contents-length"));
        }
    }
}
