package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.exception.InvalidLevellogException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.prequestion.dto.request.PreQuestionWriteRequest;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("PreQuestionController의")
class PreQuestionControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        private static final String BASE_SNIPPET_PATH = "pre-question/create/exception/";

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void save_preQuestionNullAndBlank_exception(final String preQuestion) throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest(preQuestion);

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank"));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "blank"));
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_fromNotParticipant_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");
            final String message = "같은 팀에 속해있지 않습니다.";
            willThrow(new ParticipantNotSameTeamException(DebugMessage.init()))
                    .given(preQuestionService)
                    .save(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-participant"));
        }

        @Test
        @DisplayName("내 레벨로그에 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_levellogIsMine_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");

            final String message = "잘못된 사전 질문 요청입니다.";
            willThrow(new InvalidPreQuestionException(DebugMessage.init()))
                    .given(preQuestionService)
                    .save(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "levellog-is-mine"));
        }

        @Test
        @DisplayName("사전 질문이 이미 등록되었을 때 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_preQuestionAlreadyExist_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");

            final String message = "사전 질문이 이미 존재합니다.";
            willThrow(new PreQuestionAlreadyExistException(DebugMessage.init()))
                    .given(preQuestionService)
                    .save(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "already-exist"));
        }

        private ResultActions requestCreatePreQuestion(final Long levellogId,
                                                       final PreQuestionWriteRequest request) throws Exception {
            return requestPost("/api/levellogs/" + levellogId + "/pre-questions", request);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        private static final String BASE_SNIPPET_PATH = "pre-question/update/exception/";

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void update_preQuestionNullAndBlank_exception(final String preQuestion) throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest(preQuestion);

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank"));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "blank"));
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 수정하면 예외를 던진다.")
        void update_levellogWrongId_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");

            final String message = "잘못된 레벨로그 요청입니다.";
            willThrow(new InvalidLevellogException(DebugMessage.init()
                    .append("levellogId", 1L)))
                    .given(preQuestionService)
                    .update(request, 1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_preQuestionNotFound_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");

            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(DebugMessage.init()))
                    .given(preQuestionService)
                    .update(request, 1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_fromNotMyPreQuestion_exception() throws Exception {
            // given
            final PreQuestionWriteRequest request = new PreQuestionWriteRequest("사전 질문");

            final String message = "작성자가 아닙니다.";
            willThrow(new MemberNotAuthorException(DebugMessage.init()))
                    .given(preQuestionService)
                    .update(request, 1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-pre-question"));
        }

        private ResultActions requestUpdatePreQuestion(final Long levellogId,
                                                       final Long preQuestionId,
                                                       final PreQuestionWriteRequest request) throws Exception {
            return requestPut("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId, request);
        }
    }

    @Nested
    @DisplayName("findMy 메서드는")
    class FindMy {

        @Test
        @DisplayName("레벨로그가 존재하지 않으면 예외를 던진다.")
        void findMy_levellogWrongId_exception() throws Exception {
            // given
            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(DebugMessage.init()
                    .append("levellogId", 999L)))
                    .given(preQuestionService)
                    .findMy(999L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestFindMyPreQuestion(999L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("pre-question/find-my/exception/not-exist-levellog"));
        }

        @Test
        @DisplayName("사전 질문이 존재하지 않으면 예외를 던진다.")
        void findMy_preQuestionNotFound_exception() throws Exception {
            // given
            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(DebugMessage.init()))
                    .given(preQuestionService)
                    .findMy(1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestFindMyPreQuestion(1L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("pre-question/find-my/exception/not-exist-pre-question"));
        }

        private ResultActions requestFindMyPreQuestion(final Long levellogId) throws Exception {
            return requestGet("/api/levellogs/" + levellogId + "/pre-questions/my");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        private final String BASE_SNIPPET_PATH = "pre-question/delete/exception/";

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 삭제하면 예외를 던진다.")
        void deleteById_levellogWrongId_exception() throws Exception {
            // given
            final String message = "잘못된 레벨로그 요청입니다.";
            willThrow(new InvalidLevellogException(DebugMessage.init()
                    .append("levellogId", 1L)))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_preQuestionNotFound_exception() throws Exception {
            // given
            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(DebugMessage.init()))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_fromNotMyPreQuestion_exception() throws Exception {
            // given
            final String message = "작성자가 아닙니다.";
            willThrow(new MemberNotAuthorException(DebugMessage.init()))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-pre-question"));
        }

        private ResultActions requestDeletePreQuestion(final Long levellogId, final Long preQuestionId)
                throws Exception {
            return requestDelete("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId);
        }
    }
}
