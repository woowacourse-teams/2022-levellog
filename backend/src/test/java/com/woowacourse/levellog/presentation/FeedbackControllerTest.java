package com.woowacourse.levellog.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackRequest;
import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

// FIXME : 팀 API 구현 후 수정
@Disabled
@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    // FIXME : 팀 API 구현 후 수정
    @Test
    void test() {
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @ParameterizedTest
        @NullSource
        @DisplayName("피드백 내용으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final FeedbackContentDto feedbackContentDto) throws Exception {
            // given
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/feedbacks")
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
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(study, speak, etc);
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/feedbacks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
