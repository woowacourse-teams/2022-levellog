package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("InterviewQuestionController 의")
class InterviewQuestionControllerTest extends ControllerTest {


    @Nested
    @DisplayName("save 메서드는")
    class Save {

        private static final String BASE_SNIPPET_PATH = "interview-question/save/exception/";

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void save_contentBlank_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest(" ");

            // when
            final ResultActions perform = requestCreateInterviewQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void save_interviewQuestionInvalidLength_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("a".repeat(256));
            final String message = "인터뷰 질문은 255자 이하여야합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-length"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 작성을 요청하면 예외를 던진다.")
        void save_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 왜 사용했나요?");

            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(DebugMessage.init()
                    .append("levellogId", invalidLevellogId)))
                    .given(interviewQuestionService)
                    .save(request, invalidLevellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(invalidLevellogId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-not-found"));
        }

        @Test
        @DisplayName("진행 중 상태가 아닐 때 예외가 발생한다.")
        void save_notInProgress_exception() throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 왜 사용했나요?");

            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            willThrow(new TeamNotInProgressException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, levellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-ready"));
        }

        @Test
        @DisplayName("이미 종료된 상태일 때 예외가 발생한다.")
        void save_alreadyClosed_exception() throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 왜 사용했나요?");

            final String message = "이미 인터뷰가 종료된 팀입니다.";
            willThrow(new TeamAlreadyClosedException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, levellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-closed"));
        }

        @Test
        @DisplayName("같은 팀에 속해있지 않을 때 예외가 발생한다.")
        void save_notMyTeam_exception() throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 왜 사용했나요?");

            final String message = "같은 팀에 속해있지 않습니다.";
            willThrow(new ParticipantNotSameTeamException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, levellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-team"));
        }

        @Test
        @DisplayName("자신에게 인터뷰 질문을 작성하려 할 때 예외를 던진다.")
        void save_selfInterviewQuestion_exception() throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 왜 사용했나요?");

            willThrow(new InvalidInterviewQuestionException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, levellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateInterviewQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("잘못된 인터뷰 질문 요청입니다.")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "self-interview-question"));
        }

        private ResultActions requestCreateInterviewQuestion(final Long levellogId,
                                                             final InterviewQuestionWriteRequest request)
                throws Exception {
            return requestPost("/api/levellogs/" + levellogId + "/interview-questions", request);
        }
    }

    @Nested
    @DisplayName("findAllByLevellog 메서드는")
    class FindAllByLevellog {

        private static final String BASE_SNIPPET_PATH = "interview-question/find-all-by-levellog/exception/";

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 목록 조회를 요청하면 예외를 던진다.")
        void findAllByLevellog_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(DebugMessage.init()
                    .append("levellogId", invalidLevellogId)))
                    .given(interviewQuestionService)
                    .findAllByLevellog(invalidLevellogId);

            // when
            final ResultActions perform = requestFindAllByLevellog(invalidLevellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-not-found"));
        }

        private ResultActions requestFindAllByLevellog(final Long levellogId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/interview-questions");
        }
    }

    @Nested
    @DisplayName("findAllMyInterviewQuestion 메서드는")
    class FindAllMyInterviewQuestion {

        private static final String BASE_SNIPPET_PATH = "interview-question/find-all-my-interview-question/exception/";

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 목록 조회를 요청하면 예외를 던진다.")
        void findAllMyInterviewQuestion_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(DebugMessage.init()
                    .append("levellogId", invalidLevellogId)))
                    .given(interviewQuestionService)
                    .findAllByLevellogAndAuthor(invalidLevellogId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestFindAllMyInterviewQuestion(invalidLevellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-not-found"));
        }

        private ResultActions requestFindAllMyInterviewQuestion(final Long levellogId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/interview-questions/my");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        private static final String BASE_SNIPPET_PATH = "interview-question/update/exception/";

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void update_contentBlank_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest(" ");

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void update_interviewQuestionInvalidLength_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("a".repeat(256));
            final String message = "인터뷰 질문은 255자 이하여야합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(interviewQuestionService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-length"));
        }

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 수정하면 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("수정된 인터뷰 질문");

            final String message = "인터뷰 질문이 존재하지 않습니다.";
            willThrow(new InterviewQuestionNotFoundException(DebugMessage.init()
                    .append("interviewQuestionId", invalidInterviewQuestionId)))
                    .given(interviewQuestionService)
                    .update(request, invalidInterviewQuestionId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, invalidInterviewQuestionId, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("수정된 인터뷰 질문");
            final String message = "작성자가 아닙니다.";
            willThrow(new MemberNotAuthorException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "unauthorized"));
        }

        @Test
        @DisplayName("진행 중 상태가 아닐 때 예외가 발생한다.")
        void update_notInProgress_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("수정된 인터뷰 질문");
            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            willThrow(new TeamNotInProgressException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-ready"));
        }

        @Test
        @DisplayName("이미 종료된 상태일 때 예외가 발생한다.")
        void update_alreadyClosed_exception() throws Exception {
            // given
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("수정된 인터뷰 질문");
            final String message = "이미 인터뷰가 종료된 팀입니다.";
            willThrow(new TeamAlreadyClosedException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-closed"));
        }

        private ResultActions requestUpdateInterviewQuestion(final Long levellogId, final Long interviewQuestionId,
                                                             final InterviewQuestionWriteRequest request)
                throws Exception {
            return requestPut("/api/levellogs/" + levellogId + "/interview-questions/" + interviewQuestionId, request);
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        private static final String BASE_SNIPPET_PATH = "interview-question/delete/exception/";

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 삭제하면 예외를 던진다.")
        void deleteById_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;
            final String message = "인터뷰 질문이 존재하지 않습니다.";

            willThrow(new InterviewQuestionNotFoundException(DebugMessage.init()
                    .append("interviewQuestionId", invalidInterviewQuestionId)))
                    .given(interviewQuestionService)
                    .deleteById(invalidInterviewQuestionId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteInterviewQuestion(1L, invalidInterviewQuestionId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() throws Exception {
            // given
            final String message = "작성자가 아닙니다.";
            willThrow(new MemberNotAuthorException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .deleteById(1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteInterviewQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "unauthorized"));
        }

        @Test
        @DisplayName("이미 종료된 상태일 때 예외가 발생한다.")
        void deleteById_alreadyClosed_exception() throws Exception {
            // given
            final String message = "이미 인터뷰가 종료된 팀입니다.";
            willThrow(new TeamAlreadyClosedException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .deleteById(1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteInterviewQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-closed"));
        }

        @Test
        @DisplayName("진행 중 상태가 아닐 때 예외가 발생한다.")
        void deleteById_notInProgress_exception() throws Exception {
            // given
            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            willThrow(new TeamNotInProgressException(DebugMessage.init()))
                    .given(interviewQuestionService)
                    .deleteById(1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteInterviewQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "is-ready"));
        }

        private ResultActions requestDeleteInterviewQuestion(final Long levellogId,
                                                             final Long invalidInterviewQuestionId) throws Exception {
            return requestDelete("/api/levellogs/" + levellogId + "/interview-questions/" + invalidInterviewQuestionId);
        }
    }
}
