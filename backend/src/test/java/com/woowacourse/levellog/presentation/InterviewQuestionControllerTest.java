package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionWriteDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from(" ");

            // when
            final ResultActions perform = requestCreateInterviewQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewQuestion must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void save_interviewQuestionInvalidLength_exception() throws Exception {
            // given
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("a".repeat(256));
            final String message = "인터뷰 질문은 255자 이하여야합니다.";
            willThrow(new InvalidFieldException(message))
                    .given(interviewQuestionService)
                    .save(request, 1L, 1L);

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
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("Spring을 왜 사용했나요?");

            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(message))
                    .given(interviewQuestionService)
                    .save(request, invalidLevellogId, 1L);

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

        @ParameterizedTest
        @CsvSource(value = {"이미 종료된 인터뷰입니다.,is-closed", "인터뷰 시작 전입니다.,is-ready"})
        @DisplayName("인터뷰 생성 정책에 위반되면 예외를 던진다.")
        void save_interviewTime_exception(final String message, final String snippet) throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("Spring을 왜 사용했나요?");

            willThrow(new InterviewTimeException(message))
                    .given(interviewQuestionService)
                    .save(request, levellogId, 1L);

            // when
            final ResultActions perform = requestCreateInterviewQuestion(levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + snippet));
        }

        @Test
        @DisplayName("자신에게 인터뷰 질문을 작성하려 할 때 예외를 던진다.")
        void save_selfInterviewQuestion_exception() throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("Spring을 왜 사용했나요?");

            willThrow(new InvalidInterviewQuestionException("자신의 레벨로그에 인터뷰 질문을 작성할 수 없습니다.", DebugMessage.init()))
                    .given(interviewQuestionService)
                    .save(request, levellogId, 1L);

            // when
            final ResultActions perform = requestCreateInterviewQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("자신의 레벨로그에 인터뷰 질문을 작성할 수 없습니다.")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "self-interview-question"));
        }

        private ResultActions requestCreateInterviewQuestion(final Long levellogId,
                                                             final InterviewQuestionWriteDto request) throws Exception {
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
            willThrow(new LevellogNotFoundException(message))
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
            willThrow(new LevellogNotFoundException(message))
                    .given(interviewQuestionService)
                    .findAllByLevellogAndAuthor(invalidLevellogId, 1L);

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
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from(" ");

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewQuestion must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void update_interviewQuestionInvalidLength_exception() throws Exception {
            // given
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("a".repeat(256));
            final String message = "인터뷰 질문은 255자 이하여야합니다.";
            willThrow(new InvalidFieldException(message))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

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
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("수정된 인터뷰 질문");

            final String message = "인터뷰 질문이 존재하지 않습니다.";
            willThrow(new InterviewQuestionNotFoundException(message))
                    .given(interviewQuestionService)
                    .update(request, invalidInterviewQuestionId, 1L);

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
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("수정된 인터뷰 질문");
            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

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

        @ParameterizedTest
        @CsvSource(value = {"이미 종료된 인터뷰입니다.,is-closed", "인터뷰 시작 전입니다.,is-ready"})
        @DisplayName("인터뷰 수정 정책에 위반되면 예외를 던진다.")
        void update_interviewTime_exception(final String message, final String snippet) throws Exception {
            // given
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("수정된 인터뷰 질문");
            willThrow(new InterviewTimeException(message))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestUpdateInterviewQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + snippet));
        }

        private ResultActions requestUpdateInterviewQuestion(final Long levellogId, final Long interviewQuestionId,
                                                             final InterviewQuestionWriteDto request) throws Exception {
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
            willThrow(new InterviewQuestionNotFoundException(message))
                    .given(interviewQuestionService)
                    .deleteById(invalidInterviewQuestionId, 1L);

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
            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(interviewQuestionService)
                    .deleteById(1L, 1L);

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

        @ParameterizedTest
        @CsvSource(value = {"이미 종료된 인터뷰입니다.,is-closed", "인터뷰 시작 전입니다.,is-ready"})
        @DisplayName("인터뷰 삭제 정책에 위반되면 예외를 던진다.")
        void deleteById_interviewTime_exception(final String message, final String snippet) throws Exception {
            // given
            willThrow(new InterviewTimeException(message))
                    .given(interviewQuestionService)
                    .deleteById(1L, 1L);

            // when
            final ResultActions perform = requestDeleteInterviewQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "" + snippet));
        }

        private ResultActions requestDeleteInterviewQuestion(final Long levellogId,
                                                             final Long invalidInterviewQuestionId) throws Exception {
            return requestDelete("/api/levellogs/" + levellogId + "/interview-questions/" + invalidInterviewQuestionId);
        }
    }
}
