package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("LevellogController의")
class LevellogControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        private static final String BASE_SNIPPET_PATH = "levellog/save/exception/";

        @Test
        @DisplayName("팀에서 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExists_exception() throws Exception {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("content");
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long teamId = 1L;

            final String message = "레벨로그가 이미 존재합니다.";
            willThrow(new LevellogAlreadyExistException(DebugMessage.init()
                    .append("authorId", loginStatus.getMemberId())
                    .append("teamId", teamId)))
                    .given(levellogService)
                    .save(request, loginStatus, teamId);

            // when
            final ResultActions perform = requestCreateLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "already-exists"));
        }

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void save_nameNullOrEmpty_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final LevellogWriteRequest request = new LevellogWriteRequest(" ");

            // when
            final ResultActions perform = requestCreateLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "contents"));
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 레벨로그를 저장하는 경우 예외를 던진다.")
        void save_notReady_exception() throws Exception {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("content");
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final Long teamId = 1L;

            final String message = "인터뷰 준비 상태가 아닙니다.";
            willThrow(new TeamNotReadyException(DebugMessage.init()))
                    .given(levellogService)
                    .save(request, loginStatus, teamId);

            // when
            final ResultActions perform = requestCreateLevellog(teamId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-start"));
        }

        private ResultActions requestCreateLevellog(final Long teamId, final LevellogWriteRequest request)
                throws Exception {
            return requestPost("/api/teams/" + teamId + "/levellogs", request);
        }
    }

    @Nested
    @DisplayName("find 메서드는")
    class Find {

        private static final String BASE_SNIPPET_PATH = "levellog/find/exception/";

        @Test
        @DisplayName("존재하지 않는 레벨로그를 조회하면 예외를 던진다.")
        void find_levellogNoExist_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 1000L;

            final String message = "레벨로그가 존재하지 않습니다.";
            willThrow(new LevellogNotFoundException(DebugMessage.init()
                    .append("levellogId", levellogId)))
                    .given(levellogService)
                    .findById(levellogId);

            // when
            final ResultActions perform = requestFindLevellog(teamId, levellogId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "exist"));
        }

        private ResultActions requestFindLevellog(final Long teamId, final Long levellogId) throws Exception {
            return requestGet("/api/teams/" + teamId + "/levellogs/" + levellogId);
        }

    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        private static final String BASE_SNIPPET_PATH = "levellog/update/exception/";

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void update_nameNullOrEmpty_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LevellogWriteRequest request = new LevellogWriteRequest(" ");

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("content must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "blank"));
        }

        @Test
        @DisplayName("본인이 작성하지 않은 레벨로그를 수정하려는 경우 예외를 던진다.")
        void update_unauthorized_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final LevellogWriteRequest request = new LevellogWriteRequest("update content");

            final String message = "작성자가 아닙니다.";
            willThrow(new MemberNotAuthorException(DebugMessage.init()))
                    .given(levellogService)
                    .update(request, levellogId, loginStatus);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-author"));
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 레벨로그를 수정하는 경우 예외를 던진다.")
        void update_notReady_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LoginStatus loginStatus = LoginStatus.fromLogin(1L);
            final LevellogWriteRequest request = new LevellogWriteRequest("new content");

            final String message = "인터뷰 준비 상태가 아닙니다.";
            willThrow(new TeamNotReadyException(DebugMessage.init()))
                    .given(levellogService)
                    .update(request, levellogId, loginStatus);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-start"));
        }

        private ResultActions requestUpdateLevellog(final Long teamId, final Long levellogId,
                                                    final LevellogWriteRequest request) throws Exception {
            return requestPut("/api/teams/" + teamId + "/levellogs/" + levellogId, request);
        }
    }
}
