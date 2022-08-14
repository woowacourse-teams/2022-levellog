package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("LevellogController의")
class LevellogControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("팀에서 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExists_exception() throws Exception {
            // given
            final LevellogWriteDto request = LevellogWriteDto.from("content");
            final Long authorId = 1L;
            final Long teamId = 1L;

            final String message = "팀에 레벨로그를 이미 작성했습니다.";
            willThrow(new LevellogAlreadyExistException(message)).given(levellogService)
                    .save(request, authorId, teamId);

            // when
            final ResultActions perform = requestSaveLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("levellog/save/exception-already-exists"));
        }

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void save_nameNullOrEmpty_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final LevellogWriteDto request = LevellogWriteDto.from(" ");

            // when
            final ResultActions perform = requestSaveLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document("levellog/save/exception-contents"));
        }

        @Test
        @DisplayName("팀의 인터뷰가 시작된 이후에 레벨로그를 저장하는 경우 예외를 던진다.")
        void save_afterStart_exception() throws Exception {
            // given
            final LevellogWriteDto request = LevellogWriteDto.from("content");
            final Long authorId = 1L;
            final Long teamId = 1L;

            final String message = "인터뷰 시작 전에만 레벨로그 작성이 가능합니다.";
            willThrow(new InterviewTimeException(message))
                    .given(levellogService)
                    .save(request, authorId, teamId);

            // when
            final ResultActions perform = requestSaveLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("levellog/save/exception-after-start"));
        }

        private ResultActions requestSaveLevellog(final Long teamId, final Object request) throws Exception {
            return requestPost("/api/teams/" + teamId + "/levellogs", request);
        }
    }

    @Nested
    @DisplayName("find 메서드는")
    class Find {

        @Test
        @DisplayName("존재하지 않는 레벨로그를 조회하면 예외를 던진다.")
        void find_levellogNoExist_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 1000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(message))
                    .given(levellogService)
                    .findById(levellogId);

            // when
            final ResultActions perform = requestGet("/api/teams/" + teamId + "/levellogs/" + levellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("levellog/find/exception-exist"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void update_nameNullOrEmpty_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LevellogWriteDto request = LevellogWriteDto.from(" ");

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document("levellog/update/exception-contents"));
        }

        @Test
        @DisplayName("본인이 작성하지 않은 레벨로그를 수정하려는 경우 예외를 던진다.")
        void update_unauthorized_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final Long authorId = 1L;
            final LevellogWriteDto request = LevellogWriteDto.from("update content");

            final String message = "권한이 없습니다.";
            willThrow(new UnauthorizedException(message))
                    .given(levellogService)
                    .update(request, levellogId, authorId);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("levellog/update/exception-author"));
        }

        @Test
        @DisplayName("팀의 인터뷰가 시작된 이후에 레벨로그를 수정하는 경우 예외를 던진다.")
        void update_afterStart_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final Long authorId = 1L;
            final LevellogWriteDto request = LevellogWriteDto.from("new content");

            final String message = "인터뷰 시작 전에만 레벨로그 수정이 가능합니다.";
            willThrow(new InterviewTimeException(message))
                    .given(levellogService)
                    .update(request, levellogId, authorId);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("levellog/update/exception-after-start"));
        }

        private ResultActions requestUpdateLevellog(final Long teamId, final Long levellogId, final Object request)
                throws Exception {
            return requestPut("/api/teams/" + teamId + "/levellogs/" + levellogId, request);
        }
    }
}
