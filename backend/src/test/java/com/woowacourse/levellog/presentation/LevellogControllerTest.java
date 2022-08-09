package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("LevellogController의")
class LevellogControllerTest extends ControllerTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.Y5wT9jBcP1lvMtjRqxaF0gMNDlgY5xs8SPhBKYChRn8";

    @Nested
    @DisplayName("save 메서드는")
    class SaveTest {

        @Test
        @DisplayName("팀에서 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExists_exception() throws Exception {
            // given
            final LevellogWriteDto request = LevellogWriteDto.from("content");
            final String requestContent = objectMapper.writeValueAsString(request);
            final Long authorId = 1L;
            final Long teamId = 1L;

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new LevellogAlreadyExistException("팀에 레벨로그를 이미 작성했습니다.")).when(levellogService)
                    .save(request, authorId, teamId);

            // when
            final ResultActions perform = requestSaveLevellog(teamId, requestContent);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("팀에 레벨로그를 이미 작성했습니다."));

            // docs
            perform.andDo(document("levellog/save/exception-already-exists"));
        }

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void save_nameNullOrEmpty_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final LevellogWriteDto request = LevellogWriteDto.from(" ");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);

            // when
            final ResultActions perform = requestSaveLevellog(teamId, requestContent);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("content must not be blank"));

            // docs
            perform.andDo(document("levellog/save/exception-contents"));
        }

        @Test
        @DisplayName("팀의 인터뷰가 시작된 이후에 요청하는 경우 예외를 던진다.")
        void save_afterStartTime_exception() throws Exception {
            // given
            final LevellogWriteDto request = LevellogWriteDto.from("content");
            final String requestContent = objectMapper.writeValueAsString(request);
            final Long authorId = 1L;
            final Long teamId = 1L;

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InterviewTimeException("인터뷰 시작 전에만 레벨로그 작성이 가능합니다.")).when(levellogService)
                    .save(request, authorId, teamId);

            // when
            final ResultActions perform = requestSaveLevellog(teamId, requestContent);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 시작 전에만 레벨로그 작성이 가능합니다."));

            // docs
            perform.andDo(document("levellog/save/exception-after-start"));
        }

        private ResultActions requestSaveLevellog(final Long teamId, final String requestContent) throws Exception {
            return mockMvc.perform(post("/api/teams/{teamId}/levellogs", teamId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("find 메서드는")
    class FindTest {

        @Test
        @DisplayName("존재하지 않는 레벨로그를 조회하면 예외를 던진다.")
        void find_levellogNoExist_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 1000L;

            doThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."))
                    .when(levellogService)
                    .findById(levellogId);

            // when
            final ResultActions perform = mockMvc
                    .perform(get("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("레벨로그가 존재하지 않습니다."));

            // docs
            perform.andDo(document("levellog/find/exception-exist"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class UpdateTest {

        @Test
        @DisplayName("내용으로 공백이 들어오면 예외를 던진다.")
        void update_nameNullOrEmpty_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LevellogWriteDto request = LevellogWriteDto.from(" ");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, requestContent);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("content must not be blank"));

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
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new UnauthorizedException("권한이 없습니다.")).when(levellogService)
                    .update(request, levellogId, authorId);

            // when
            final ResultActions perform = requestUpdateLevellog(teamId, levellogId, requestContent);

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("levellog/update/exception-author"));
        }

        private ResultActions requestUpdateLevellog(final Long teamId, final Long levellogId,
                                                    final String requestContent)
                throws Exception {
            return mockMvc.perform(
                            put("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class DeleteTest {

        @Test
        @DisplayName("본인이 작성하지 않은 레벨로그를 삭제하려는 경우 예외를 던진다.")
        void delete_nameNullOrEmpty_Exception() throws Exception {
            // given
            final Long teamId = 1L;
            final Long memberId = 1L;
            final Long levellogId = 2L;

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new UnauthorizedException("권한이 없습니다.")).when(levellogService)
                    .deleteById(levellogId, memberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("levellog/delete/exception-author"));
        }
    }
}
