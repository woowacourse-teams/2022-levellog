package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
    private final Long memberId = 1L;

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new FeedbackAlreadyExistException("피드백이 이미 존재합니다."));

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
            perform.andDo(document("feedback/save/exception/exist"));
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(" [levellogId : " + levellogId + "]",
                            "자기 자신에게 피드백을 할 수 없습니다."));

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
            perform.andDo(document("feedback/save/exception/self"));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(
                            " [memberId :" + memberId + "]", "같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다."));

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
            perform.andDo(document("feedback/save/exception/team"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 작성을 요청하면 예외가 발생한다.")
        void save_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 20000000L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new LevellogNotFoundException("존재하지 않는 레벨로그"));

            // when
            final ResultActions performCreate = mockMvc.perform(
                            post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            performCreate.andExpect(status().isNotFound());

            // docs
            performCreate.andDo(document("feedback/save/exception/levellog"));
        }

        @Test
        @DisplayName("팀 인터뷰 시작 전에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_beforeStartAt_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다."));

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
            perform.andDo(document("feedback/save/exception/before-interview"));
        }

        @Test
        @DisplayName("팀 인터뷰 종료 후에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_alreadyClosed_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException("이미 종료된 인터뷰입니다."));

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
            perform.andDo(document("feedback/save/exception/after-interview"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 피드백을 수정하면 예외가 발생한다.")
        void update_otherMember_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            willThrow(new InvalidFeedbackException(
                    " [feedbackId : " + feedbackId + ", memberId : " + memberId + "]", "자신이 남긴 피드백만 수정할 수 있습니다."))
                    .given(feedbackService)
                    .update(request, levellogId, feedbackId, memberId);

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
            perform.andDo(document("feedback/update/exception/author"));
        }

        @Test
        @DisplayName("존재하지 않는 피드백 정보로 피드백 수정을 요청하면 예외가 발생한다.")
        void update_notFoundFeedback_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 1000000L;

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            willThrow(new FeedbackNotFoundException("존재하지 않는 피드백"))
                    .given(feedbackService)
                    .update(request, levellogId, feedbackId, memberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound());

            // docs
            perform.andDo(document("feedback/update/exception/feedback"));
        }

        @Test
        @DisplayName("인터뷰 시작 전에 피드백을 수정하면 예외가 발생한다.")
        void update_beforeStartAt_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            willThrow(new InterviewTimeException("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다."))
                    .given(feedbackService)
                    .update(request, levellogId, feedbackId, memberId);

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
            perform.andDo(document("feedback/update/exception/before-interview"));
        }

        @Test
        @DisplayName("인터뷰 종료 후에 피드백을 수정하면 예외가 발생한다.")
        void update_alreadyClosed_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);
            final String requestContent = objectMapper.writeValueAsString(request);

            willThrow(new InterviewTimeException("이미 종료된 인터뷰입니다."))
                    .given(feedbackService)
                    .update(request, levellogId, feedbackId, memberId);

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
            perform.andDo(document("feedback/update/exception/after-interview"));
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
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            doThrow(new InvalidFeedbackException(
                    " [feedbackId : " + feedbackId + ", memberId : " + memberId + "]", "자신이 남긴 피드백만 삭제할 수 있습니다."))
                    .when(feedbackService)
                    .deleteById(feedbackId, memberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("feedback/delete/exception/author"));
        }

        @Test
        @DisplayName("존재하지 않는 피드백 정보로 피드백 삭제를 요청하면 예외가 발생한다.")
        void delete_notFoundFeedback_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2000000L;
            doThrow(new FeedbackNotFoundException("존재하지 않는 피드백"))
                    .when(feedbackService)
                    .deleteById(feedbackId, memberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound());

            // docs
            perform.andDo(document("feedback/delete/exception/feedback"));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class findAll {

        @Test
        @DisplayName("존재하지 않는 멤버에 대한 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findAll_notFoundMember_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;

            given(feedbackService.findAll(1L, 1L))
                    .willThrow(new MemberNotFoundException("존재하지 않는 멤버"));

            // when
            final ResultActions perform = mockMvc.perform(
                            get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound());

            // docs
            perform.andDo(document("feedback/find-all/exception/member"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findAll_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 200000L;

            given(feedbackService.findAll(levellogId, 1L))
                    .willThrow(new LevellogNotFoundException("존재하지 않는 레벨로그"));

            // when
            final ResultActions performFindAll = mockMvc.perform(
                            get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            performFindAll.andExpect(status().isNotFound());

            // docs
            performFindAll.andDo(document("feedback/find-all/exception/levellog"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() throws Exception {
            // given
            final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
            final Long levellogId = 1L;

            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);
            given(feedbackService.findAll(levellogId, 1L))
                    .willThrow(new UnauthorizedException("권한이 없습니다."));

            // when
            final ResultActions perform = mockMvc.perform(get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized());

            // docs
            perform.andDo(document("feedback/find-all/exception/not-my-team"));
        }
    }
}
