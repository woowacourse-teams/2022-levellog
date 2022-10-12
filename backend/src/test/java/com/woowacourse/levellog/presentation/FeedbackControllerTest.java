package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.dto.request.FeedbackWriteRequest;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.exception.InvalidLevellogException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("FeedbackController의")
class FeedbackControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        private static final String BASE_SNIPPET_PATH = "feedback/save/exception/";

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "피드백이 이미 존재합니다.";
            given(feedbackService.save(request, levellogId, loginStatus))
                    .willThrow(new FeedbackAlreadyExistException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "exist"));
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "잘못된 피드백 요청입니다.";
            given(feedbackService.save(request, levellogId, loginStatus))
                    .willThrow(new InvalidFeedbackException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "self"));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "같은 팀에 속해있지 않습니다.";
            given(feedbackService.save(request, levellogId, loginStatus))
                    .willThrow(new ParticipantNotSameTeamException(DebugMessage.init()));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "team"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 작성을 요청하면 예외가 발생한다.")
        void save_notFoundLevellog_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 20000000L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.save(request, levellogId, loginStatus)).willThrow(
                    new LevellogNotFoundException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog"));
        }

        @Test
        @DisplayName("팀 인터뷰 시작 전에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_notInProgress_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            given(feedbackService.save(request, levellogId, loginStatus))
                    .willThrow(new TeamNotInProgressException(DebugMessage.init()));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "before-interview"));
        }

        @Test
        @DisplayName("팀 인터뷰 종료 후에 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_alreadyClosed_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "이미 인터뷰가 종료된 팀입니다.";
            given(feedbackService.save(request, levellogId, loginStatus))
                    .willThrow(new TeamAlreadyClosedException(DebugMessage.init()));

            // when
            final ResultActions perform = requestCreateFeedback(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-interview"));
        }

        private ResultActions requestCreateFeedback(final Long levellogId, final FeedbackWriteRequest request)
                throws Exception {
            return requestPost("/api/levellogs/" + levellogId + "/feedbacks", request);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        private static final String BASE_SNIPPET_PATH = "feedback/update/exception/";

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 피드백을 수정하면 예외가 발생한다.")
        void update_otherMember_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "잘못된 피드백 요청입니다.";
            willThrow(new InvalidFeedbackException(DebugMessage.init()
                    .append("feedbackId", feedbackId)
                    .append("memberId", loginStatus.getMemberId())))
                    .given(feedbackService)
                    .update(request, feedbackId, loginStatus);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-author"));
        }

        @Test
        @DisplayName("존재하지 않는 피드백 정보로 피드백 수정을 요청하면 예외가 발생한다.")
        void update_notFoundFeedback_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final Long feedbackId = 1000000L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "피드백이 존재하지 않습니다.";
            willThrow(new FeedbackNotFoundException(DebugMessage.init()
                    .append("feedbackId", feedbackId)))
                    .given(feedbackService)
                    .update(request, feedbackId, loginStatus);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "feedback"));
        }

        @Test
        @DisplayName("팀 진행 상태가 아닐 때 피드백을 수정하면 예외가 발생한다.")
        void update_notInProgress_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            willThrow(new TeamNotInProgressException(DebugMessage.init()))
                    .given(feedbackService)
                    .update(request, feedbackId, loginStatus);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "before-interview"));
        }

        @Test
        @DisplayName("인터뷰 종료 후에 피드백을 수정하면 예외가 발생한다.")
        void update_alreadyClosed_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;
            final Long feedbackId = 2L;
            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            final String message = "이미 인터뷰가 종료된 팀입니다.";
            willThrow(new TeamAlreadyClosedException(DebugMessage.init()))
                    .given(feedbackService)
                    .update(request, feedbackId, loginStatus);

            // when
            final ResultActions perform = requestUpdateFeedback(levellogId, feedbackId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-interview"));
        }

        private ResultActions requestUpdateFeedback(final Long levellogId, final Long feedbackId,
                                                    final FeedbackWriteRequest request) throws Exception {
            return requestPut("/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId, request);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        private static final String BASE_SNIPPET_PATH = "feedback/find-all/exception/";

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findAll_notFoundLevellog_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 200000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.findAll(levellogId, loginStatus))
                    .willThrow(new LevellogNotFoundException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestFindAllFeedback(levellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long levellogId = 1L;

            final String message = "같은 팀에 속해있지 않습니다.";
            given(feedbackService.findAll(levellogId, loginStatus))
                    .willThrow(new ParticipantNotSameTeamException(DebugMessage.init()));

            // when
            final ResultActions perform = requestFindAllFeedback(levellogId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-team"));
        }

        private ResultActions requestFindAllFeedback(final Long levellogId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/feedbacks");
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private static final String BASE_SNIPPET_PATH = "feedback/find-by-id/exception/";

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 피드백 목록 조회를 요청하면 예외가 발생한다.")
        void findById_notFoundLevellog_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long feedbackId = 1L;
            final Long levellogId = 200000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            given(feedbackService.findById(levellogId, feedbackId, loginStatus))
                    .willThrow(new LevellogNotFoundException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-not-found"));
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findById_notMyTeam_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long feedbackId = 1L;
            final Long levellogId = 1L;

            final String message = "같은 팀에 속해있지 않습니다.";
            given(feedbackService.findById(levellogId, feedbackId, loginStatus))
                    .willThrow(new ParticipantNotSameTeamException(DebugMessage.init()));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-team"));
        }

        @Test
        @DisplayName("잘못된 레벨로그의 피드백 조회를 요청하면 예외가 발생한다.")
        void findById_levellogWrongId_exception() throws Exception {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long feedbackId = 1L;
            final Long levellogId = 1L;

            final String message = "잘못된 레벨로그 요청입니다.";
            given(feedbackService.findById(levellogId, feedbackId, loginStatus))
                    .willThrow(new InvalidLevellogException(DebugMessage.init()
                            .append("levellogId", levellogId)));

            // when
            final ResultActions perform = requestFindByIdFeedback(levellogId, feedbackId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-wrong-id"));
        }

        private ResultActions requestFindByIdFeedback(final Long levellogId, final Long feedbackId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/feedbacks/" + feedbackId);
        }
    }
}
