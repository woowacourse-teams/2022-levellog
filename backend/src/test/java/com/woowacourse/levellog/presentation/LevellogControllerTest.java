package com.woowacourse.levellog.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.support.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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
            Long teamId = 1L;
            final LevellogRequest request = new LevellogRequest(content);
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

            Long teamId = 1L;
            Long levellogId = 1000L;

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
            Long teamId = 1L;
            Long levellogId = 2L;
            final LevellogRequest request = new LevellogRequest(content);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}


