package com.woowacourse.levellog.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @ParameterizedTest
        @NullSource
        @DisplayName("피드백 내용으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final FeedbackContentDto feedbackContentDto) throws Exception {
            // given
            final Long levellogId = 1L;
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @CsvSource(value = {"null,speak,etc", "study,null,etc", "study,speak,null"}, nullValues = {"null"})
        @DisplayName("피드백 내용의 항목으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final String study, final String speak, final String etc) throws Exception {
            // given
            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(study, speak, etc);
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @ParameterizedTest
        @NullSource
        @DisplayName("피드백 내용으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final FeedbackContentDto feedbackContentDto) throws Exception {
            // given
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @CsvSource(value = {"null,speak,etc", "study,null,etc", "study,speak,null"}, nullValues = {"null"})
        @DisplayName("피드백 내용의 항목으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final String study, final String speak, final String etc) throws Exception {
            // given
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(study, speak, etc);
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
