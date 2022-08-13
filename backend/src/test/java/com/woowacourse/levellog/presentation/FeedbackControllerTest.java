package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    private static final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";
    private final Long memberId = 1L;

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new FeedbackAlreadyExistException("피드백이 이미 존재합니다."));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("피드백이 이미 존재합니다.")
            );

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
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(
                            "자기 자신에게 피드백을 할 수 없습니다.", " [levellogId : " + levellogId + "]"));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("자기 자신에게 피드백을 할 수 없습니다.")
            );

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
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(
                            "같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다.", " [memberId :" + memberId + "]"));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다.")
            );

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
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("레벨로그가 존재하지 않습니다.")
            );

            // docs
            perform.andDo(document("feedback/save/exception/levellog"));
        }

        @Test
        @DisplayName("팀 인터뷰 시작 전에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_beforeStartAt_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다."));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.")
            );

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
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException("이미 종료된 인터뷰입니다."));

            // when
            final ResultActions perform = requestPost("/api/levellogs/" + levellogId + "/feedbacks", token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("이미 종료된 인터뷰입니다.")
            );

            // docs
            perform.andDo(document("feedback/save/exception/after-interview"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 피드백을 수정하면 예외가 발생한다.")
        void update_otherMember_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;

            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            willThrow(new InvalidFeedbackException("자신이 남긴 피드백만 수정할 수 있습니다.",
                    " [feedbackId : " + feedbackId + ", memberId : " + memberId + "]"))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestPut(
                    "/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("자신이 남긴 피드백만 수정할 수 있습니다.")
            );

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

            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            willThrow(new FeedbackNotFoundException("존재하지 않는 피드백입니다."))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestPut(
                    "/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, token, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("존재하지 않는 피드백입니다.")
            );

            // docs
            perform.andDo(document("feedback/update/exception/feedback"));
        }

        @Test
        @DisplayName("인터뷰 시작 전에 피드백을 수정하면 예외가 발생한다.")
        void update_beforeStartAt_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;
            final Long feedbackId = 2L;

            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            willThrow(new InterviewTimeException("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다."))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestPut(
                    "/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, token, request);
            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.")
            );

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

            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            willThrow(new InterviewTimeException("이미 종료된 인터뷰입니다."))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestPut(
                    "/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, token, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("이미 종료된 인터뷰입니다.")
            );

            // docs
            perform.andDo(document("feedback/update/exception/after-interview"));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findAll_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 200000L;

            given(feedbackService.findAll(levellogId, memberId))
                    .willThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."));

            // when
            final ResultActions perform = requestGet("/api/levellogs/" + levellogId + "/feedbacks", token);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("레벨로그가 존재하지 않습니다.")
            );

            // docs
            perform.andDo(document("feedback/find-all/exception/levellog"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(token)).willReturn("1");
            given(jwtTokenProvider.validateToken(token)).willReturn(true);

            final Long levellogId = 1L;

            given(feedbackService.findAll(levellogId, memberId))
                    .willThrow(new UnauthorizedException("권한이 없습니다."));

            // when
            final ResultActions perform = requestGet("/api/levellogs/" + levellogId + "/feedbacks", token);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value("권한이 없습니다.")
            );

            // docs
            perform.andDo(document("feedback/find-all/exception/not-my-team"));
        }
    }
}
