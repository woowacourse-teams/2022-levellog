package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.levellog.dto.LevellogCreateDto;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.levellog.exception.UnauthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("LevellogController의")
class LevellogControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("내용으로 공백이나 null이 들어오면 예외를 던진다.")
        void nameNullOrEmpty_Exception(final String content) throws Exception {
            // given
            final Long teamId = 1L;
            final LevellogCreateDto request = new LevellogCreateDto(content);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams/{teamId}/levellogs", teamId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("find 메서드는")
    class find {

        @Test
        @DisplayName("존재하지 않는 레벨로그를 조회하면 404 예외를 던진다.")
        void levellogNoExist_Exception() throws Exception {
            // given
            doThrow(new LevellogNotFoundException())
                    .when(levellogService)
                    .findById(any());

            final Long teamId = 1L;
            final Long levellogId = 1000L;

            // when
            final ResultActions perform = mockMvc
                    .perform(get("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("내용으로 공백이나 null이 들어오면 예외를 던진다.")
        void nameNullOrEmpty_Exception(final String content) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("123");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);

            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LevellogCreateDto request = new LevellogCreateDto(content);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer: token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("본인이 작성하지 않은 레벨로그를 수정하려는 경우 예외를 던진다.")
        void unauthorized_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("123");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);
            doThrow(new UnauthorizedException("레벨로그를 수정할 권한이 없습니다.")).when(levellogService)
                    .update(any(), anyLong(), anyLong());

            final Long teamId = 1L;
            final Long levellogId = 2L;
            final LevellogCreateDto request = new LevellogCreateDto("content");
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer: token")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("본인이 작성하지 않은 레벨로그를 삭제하려는 경우 예외를 던진다.")
        void nameNullOrEmpty_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(anyString())).willReturn("123");
            given(jwtTokenProvider.validateToken(any())).willReturn(true);
            doThrow(new UnauthorizedException("레벨로그를 삭제할 권한이 없습니다.")).when(levellogService)
                    .deleteById(anyLong(), anyLong());

            final Long teamId = 1L;
            final Long levellogId = 2L;

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer: token"))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized());
        }
    }
}


