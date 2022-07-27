package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.feedback.dto.CreateFeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
        void feedback_contentNull_exceptionThrown(final FeedbackContentDto feedbackContentDto) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestContent));

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @CsvSource(value = {"null,speak,etc", "study,null,etc", "study,speak,null"}, nullValues = {"null"})
        @DisplayName("피드백 내용의 항목으로 null이 들어오면 예외를 던진다.")
        void feedbackContentNull_Exception(final String study, final String speak, final String etc) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(study, speak, etc);
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            given(feedbackService.save(any(CreateFeedbackDto.class), anyLong(), anyLong()))
                    .willThrow(new FeedbackAlreadyExistException(levellogId));

            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            given(feedbackService.save(any(CreateFeedbackDto.class), anyLong(), anyLong()))
                    .willThrow(new InvalidFeedbackException("자기 자신에게 피드백을 할 수 없습니다."));

            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            given(feedbackService.save(any(CreateFeedbackDto.class), anyLong(), anyLong()))
                    .willThrow(new InvalidFeedbackException("같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다."));

            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer token")
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
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
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
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(study, speak, etc);
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 삭제를 요청하면 예외가 발생한다.")
        void delete_otherMember_exceptionThrown() throws Exception {// given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            doThrow(new InvalidFeedbackException("자신이 남기거나 받은 피드백만 삭제할 수 있습니다."))
                    .when(feedbackService)
                    .deleteById(anyLong(), anyLong());

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer token"))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
