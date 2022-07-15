package com.woowacourse.levellog.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("이름으로 공백이나 null이 들어오면 예외를 던진다.")
        void nameNullOrEmpty_Exception(final String name) throws Exception {
            // given
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                    "아이 컨텍이 좋습니다.",
                    "윙크하지 마세요.");
            final FeedbackCreateRequest request = new FeedbackCreateRequest(name, feedbackContentDto);
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
        @NullSource
        @DisplayName("피드백 내용으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final FeedbackContentDto feedbackContentDto) throws Exception {
            // given
            final FeedbackCreateRequest request = new FeedbackCreateRequest("로마", feedbackContentDto);
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
            final FeedbackCreateRequest request = new FeedbackCreateRequest("로마", feedbackContentDto);
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
