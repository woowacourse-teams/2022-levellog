package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
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
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/save/exception-exist"));
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
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
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/save/exception-self"));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
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
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/save/exception-team"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 피드백을 수정하면 예외가 발생한다.")
        void delete_otherMember_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            doThrow(new InvalidFeedbackException("자신이 남긴 피드백만 수정할 수 있습니다."))
                    .when(feedbackService)
                    .update(any(CreateFeedbackDto.class), anyLong(), anyLong());

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/update/exception-author"));
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 삭제를 요청하면 예외가 발생한다.")
        void delete_otherMember_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(anyString())).willReturn("1");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            doThrow(new InvalidFeedbackException("자신이 남긴 피드백만 삭제할 수 있습니다."))
                    .when(feedbackService)
                    .deleteById(anyLong(), anyLong());

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/delete/exception-author"));
        }
    }
}
