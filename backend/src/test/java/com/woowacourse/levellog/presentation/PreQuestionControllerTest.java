package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
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

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void save_PreQuestionNullAndBlank_Exception(final String preQuestion) throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("preQuestion must not be blank"));

            // docs
            perform.andDo(document("pre-question/create/exception/null-and-blank"));
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_FromNotParticipant_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");
            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(preQuestionService)
                    .save(preQuestionDto, 1L, 1L);

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/create/exception/not-participant"));
        }

        @Test
        @DisplayName("내 레벨로그에 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_LevellogIsMine_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            final String message = "자기 자신에게 사전 질문을 등록할 수 없습니다.";
            willThrow(new InvalidPreQuestionException("[levellogId : 1]", message))
                    .given(preQuestionService)
                    .save(preQuestionDto, 1L, 1L);

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/create/exception/levellog-is-mine"));
        }

        @Test
        @DisplayName("사전 질문이 이미 등록되었을 때 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_PreQuestionAlreadyExist_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            final String message = "레벨로그의 사전 질문을 이미 작성했습니다.";
            willThrow(new PreQuestionAlreadyExistException(message))
                    .given(preQuestionService)
                    .save(preQuestionDto, 1L, 1L);

            // when
            final ResultActions perform = requestCreatePreQuestion(1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/create/exception/already-exist"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void update_PreQuestionNullAndBlank_Exception(final String preQuestion) throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("preQuestion must not be blank"));

            // docs
            perform.andDo(document("pre-question/update/exception/null-and-blank"));
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 수정하면 예외를 던진다.")
        void update_LevellogWrongId_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            final String message = "입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1";
            willThrow(new InvalidFieldException(message))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 1L);

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/update/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_PreQuestionNotFound_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(message))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 1L);

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/update/exception/notfound"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_FromNotMyPreQuestion_Exception() throws Exception {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 1L);

            // when
            final ResultActions perform = requestUpdatePreQuestion(1L, 1L, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message));

            // docs
            perform.andDo(document("pre-question/update/exception/not-my-pre-question"));
        }
    }

    @Nested
    @DisplayName("findMy 메서드는")
    class FindMy {

        @Test
        @DisplayName("레벨로그가 존재하지 않으면 예외를 던진다.")
        void findMy_LevellogWrongId_Exception() throws Exception {
            // given
            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(message))
                    .given(preQuestionService)
                    .findMy(999L, 1L);

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
        void findMy_PreQuestionNotFound_Exception() throws Exception {
            // given
            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(message))
                    .given(preQuestionService)
                    .findMy(1L, 1L);

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
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 삭제하면 예외를 던진다.")
        void deleteById_LevellogWrongId_Exception() throws Exception {
            // given
            final String message = "입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1";
            willThrow(new InvalidFieldException(message))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 1L);

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("pre-question/delete/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_PreQuestionNotFound_Exception() throws Exception {
            // given
            final String message = "사전 질문이 존재하지 않습니다.";
            willThrow(new PreQuestionNotFoundException(message))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 1L);

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("pre-question/delete/exception/notfound"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_FromNotMyPreQuestion_Exception() throws Exception {
            // given
            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 1L);

            // when
            final ResultActions perform = requestDeletePreQuestion(1L, 1L);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("pre-question/delete/exception/not-my-pre-question"));
        }
    }

    private ResultActions requestCreatePreQuestion(final Long levellogId,
                                                   final PreQuestionDto preQuestionDto) throws Exception {
        return requestPost("/api/levellogs/" + levellogId + "/pre-questions", preQuestionDto);
    }

    private ResultActions requestFindMyPreQuestion(final Long levellogId) throws Exception {
        return requestGet("/api/levellogs/" + levellogId + "/pre-questions/my");
    }

    private ResultActions requestUpdatePreQuestion(final Long levellogId,
                                                   final Long preQuestionId,
                                                   final PreQuestionDto request) throws Exception {
        return requestPut("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId, request);
    }

    private ResultActions requestDeletePreQuestion(final Long levellogId, final Long preQuestionId) throws Exception {
        return requestDelete("/api/levellogs/" + levellogId + "/pre-questions/" + preQuestionId);
    }
}
