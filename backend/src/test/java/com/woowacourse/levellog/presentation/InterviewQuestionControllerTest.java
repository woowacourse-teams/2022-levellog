package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
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


    private String getUrl(final long levellogId) {
        return "/api/levellogs/" + levellogId + "/interview-questions";
    }

    private String getUrl(final long levellogId, final long interviewQuestionId) {
        return "/api/levellogs/" + levellogId + "/interview-questions/" + interviewQuestionId;
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void save_contentBlank_exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from(" ");

            // when
            final ResultActions perform = requestPost(getUrl(1), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("interviewQuestion must not be blank"));

            // docs
            perform.andDo(document("interview-question/save/exception/contents/blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void save_interviewQuestionInvalidLength_Exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("a".repeat(256));
            willThrow(new InvalidFieldException("인터뷰 질문은 255자 이하여야합니다."))
                    .given(interviewQuestionService)
                    .save(request, 1L, 1L);

            // when
            final ResultActions perform = requestPost(getUrl(1), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 질문은 255자 이하여야합니다."));

            // docs
            perform.andDo(document("interview-question/save/exception/contents/length"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 작성을 요청하면 예외를 던진다.")
        void save_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?");

            willThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."))
                    .given(interviewQuestionService)
                    .save(request, invalidLevellogId, 1L);

            // when
            final ResultActions perform = requestPost(getUrl(invalidLevellogId), request);

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("레벨로그가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/save/exception/levellog-not-found"));
        }

        @ParameterizedTest
        @CsvSource(value = {"이미 종료된 인터뷰입니다.,is-closed", "인터뷰 시작 전입니다.,is-ready"})
        @DisplayName("인터뷰 생성 정책에 위반되면 예외를 던진다.")
        void save_interviewTime_exception(final String message, final String snippet) throws Exception {
            // given
            final long levellogId = 1L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?");

            willThrow(new InterviewTimeException(message))
                    .given(interviewQuestionService)
                    .save(request, levellogId, 1L);

            // when
            final ResultActions perform = requestPost(getUrl(levellogId), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value(message));

            // docs
            perform.andDo(document("interview-question/save/exception/" + snippet));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 목록 조회를 요청하면 예외를 던진다.")
        void findAll_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            willThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."))
                    .given(interviewQuestionService)
                    .findAllByLevellogAndAuthor(invalidLevellogId, 1L);

            // when
            final ResultActions perform = requestGet(getUrl(invalidLevellogId));

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("레벨로그가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/findAll/exception/levellog-not-found"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void update_contentBlank_exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from(" ");

            // when
            final ResultActions perform = requestPut(getUrl(1L, 1L), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("interviewQuestion must not be blank"));

            // docs
            perform.andDo(document("interview-question/update/exception/contents/blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void update_interviewQuestionInvalidLength_Exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("a".repeat(256));
            willThrow(new InvalidFieldException("인터뷰 질문은 255자 이하여야합니다."))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestPut(getUrl(1L, 1L), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 질문은 255자 이하여야합니다."));

            // docs
            perform.andDo(document("interview-question/update/exception/contents/length"));
        }

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 수정하면 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");

            willThrow(new InterviewQuestionNotFoundException("인터뷰 질문이 존재하지 않습니다."))
                    .given(interviewQuestionService)
                    .update(request, invalidInterviewQuestionId, 1L);

            // when
            final ResultActions perform = requestPut(getUrl(1L, invalidInterviewQuestionId), request);

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("인터뷰 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/update/exception/not-found"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");
            willThrow(new UnauthorizedException("권한이 없습니다."))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestPut(getUrl(1L, 1L), request);

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("interview-question/update/exception/unauthorized"));
        }

        @ParameterizedTest
        @CsvSource(value = {"이미 종료된 인터뷰입니다.,is-closed", "인터뷰 시작 전입니다.,is-ready"})
        @DisplayName("인터뷰 수정 정책에 위반되면 예외를 던진다.")
        void update_interviewTime_exception(final String message, final String snippet) throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");
            willThrow(new InterviewTimeException(message))
                    .given(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestPut(getUrl(1L, 1L), request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value(message));

            // docs
            perform.andDo(document("interview-question/update/exception/" + snippet));
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 삭제하면 예외를 던진다.")
        void deleteById_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;
            willThrow(new InterviewQuestionNotFoundException("인터뷰 질문이 존재하지 않습니다."))
                    .given(interviewQuestionService)
                    .deleteById(invalidInterviewQuestionId, 1L);

            // when
            final ResultActions perform = requestDelete(getUrl(1L, invalidInterviewQuestionId));

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("인터뷰 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/delete/exception/not-found"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() throws Exception {
            // given
            willThrow(new UnauthorizedException("권한이 없습니다."))
                    .given(interviewQuestionService)
                    .deleteById(1L, 1L);

            // when
            final ResultActions perform = requestDelete(getUrl(1L, 1L));

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("interview-question/delete/exception/unauthorized"));
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
            final ResultActions perform = requestDelete(getUrl(1L, 1L));

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value(message));

            // docs
            perform.andDo(document("interview-question/delete/exception/" + snippet));
        }
    }
}
