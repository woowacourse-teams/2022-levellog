package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
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

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "피드백이 이미 존재합니다.";
            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new FeedbackAlreadyExistException(message));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/exist"));
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "자기 자신에게 피드백을 할 수 없습니다.";
            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(message, " [levellogId : " + levellogId + "]"));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/self"));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다.";
            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InvalidFeedbackException(message, " [memberId :" + memberId + "]"));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/team"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 작성을 요청하면 예외가 발생한다.")
        void save_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 20000000L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.save(request, levellogId, memberId)).willThrow(
                    new LevellogNotFoundException(message));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/levellog"));
        }

        @Test
        @DisplayName("팀 인터뷰 시작 전에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_beforeStartAt_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.";
            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException(message));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/before-interview"));
        }

        @Test
        @DisplayName("팀 인터뷰 종료 후에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_alreadyClosed_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "이미 종료된 인터뷰입니다.";
            given(feedbackService.save(request, levellogId, memberId))
                    .willThrow(new InterviewTimeException(message));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/save/exception/after-interview"));
        }

        private ResultActions requestCreateFeedback(final Long levellogId, final FeedbackWriteDto request)
                throws Exception {
            return requestPost("/api/levellogs/" + levellogId + "/feedbacks", request);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 피드백을 수정하면 예외가 발생한다.")
        void update_otherMember_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "자신이 남긴 피드백만 수정할 수 있습니다.";
            willThrow(new InvalidFeedbackException(
                    message, " [feedbackId : " + feedbackId + ", memberId : " + memberId + "]"))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/update/exception/author"));
        }

        @Test
        @DisplayName("존재하지 않는 피드백 정보로 피드백 수정을 요청하면 예외가 발생한다.")
        void update_notFoundFeedback_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final Long feedbackId = 1000000L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "존재하지 않는 피드백입니다.";
            willThrow(new FeedbackNotFoundException(message))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/update/exception/feedback"));
        }

        @Test
        @DisplayName("인터뷰 시작 전에 피드백을 수정하면 예외가 발생한다.")
        void update_beforeStartAt_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.";
            willThrow(new InterviewTimeException(message))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/update/exception/before-interview"));
        }

        @Test
        @DisplayName("인터뷰 종료 후에 피드백을 수정하면 예외가 발생한다.")
        void update_alreadyClosed_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteDto request = FeedbackWriteDto.from(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "이미 종료된 인터뷰입니다.";
            willThrow(new InterviewTimeException(message))
                    .given(feedbackService)
                    .update(request, feedbackId, memberId);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/update/exception/after-interview"));
        }

        private ResultActions requestUpdateFeedback(final Long levellogId, final Long feedbackId,
                                                    final FeedbackWriteDto request) throws Exception {
            return requestPut("/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, request);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findAll_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 200000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.findAll(levellogId, memberId))
                    .willThrow(new LevellogNotFoundException(message));

            // when
            final ResultActions perform = requestFindAllFeedback(levellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/find-all/exception/levellog"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() throws Exception {
            // given
            final Long memberId = 1L;
            final Long levellogId = 1L;

            final String message = "권한이 없습니다.";
            given(feedbackService.findAll(levellogId, memberId))
                    .willThrow(new UnauthorizedException(message));

            // when
            final ResultActions perform = requestFindAllFeedback(levellogId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/find-all/exception/not-my-team"));
        }

        private ResultActions requestFindAllFeedback(final Long levellogId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/feedbacks");
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findById_notFoundLevellog_exceptionThrown() throws Exception {
            // given
            final Long memberId = 1L;
            final Long feedbackId = 1L;
            final Long levellogId = 200000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.findById(levellogId, feedbackId, memberId))
                    .willThrow(new LevellogNotFoundException(message));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/find-by-id/exception/levellog-not-found"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findById_notMyTeam_exception() throws Exception {
            // given
            final Long memberId = 1L;
            final Long feedbackId = 1L;
            final Long levellogId = 1L;

            final String message = "권한이 없습니다.";
            given(feedbackService.findById(levellogId, feedbackId, memberId))
                    .willThrow(new UnauthorizedException(message));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/find-by-id/exception/not-my-team"));
        }

        @Test
        @DisplayName("잘못된 레벨로그의 피드백 조회를 요청하면 예외가 발생한다.")
        void findById_levellogWrongId_exception() throws Exception {
            // given
            final Long memberId = 1L;
            final Long feedbackId = 1L;
            final Long levellogId = 1L;

            final String message = "입력한 levellogId와 피드백의 levellogId가 다릅니다.";
            given(feedbackService.findById(levellogId, feedbackId, memberId))
                    .willThrow(new InvalidFieldException(message));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("feedback/find-by-id/exception/levellog-wrong-id"));
        }

        private ResultActions requestFindByIdFeedback(final Long levellogId, final Long feedbackId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId);
        }
    }
}
