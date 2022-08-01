package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("PreQuestionController의")
public class PreQuestionControllerTest extends ControllerTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void preQuestionNull_Exception(final String preQuestion) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);
            final String requestContent = objectMapper.writeValueAsString(preQuestionDto);

            doThrow(new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다."))
                    .when(preQuestionService)
                    .save(preQuestionDto, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/1/pre-questions")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("preQuestion must not be blank");

            // docs
            perform.andDo(document("pre-question/create/exception/null-and-blank"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문으로 공백이나 null이 들어오면 예외를 던진다.")
        void preQuestionNull_Exception(final String preQuestion) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);
            final String requestContent = objectMapper.writeValueAsString(preQuestionDto);

            doThrow(new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다."))
                    .when(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("preQuestion must not be blank");

            // docs
            perform.andDo(document("pre-question/update/exception/null-and-blank"));
        }

        @Test
        @DisplayName("url에서 잘못된 레벨로그 id를 입력하면 예외를 던진다.")
        void preQuestionWrongLevellogId_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = new PreQuestionDto("사전 질문");
            final String requestContent = objectMapper.writeValueAsString(preQuestionDto);

            doThrow(new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .when(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString()
                    .contains("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1");

            // docs
            perform.andDo(document("pre-question/update/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("없는 사전 질문을 수정하면 예외를 던진다.")
        void preQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = new PreQuestionDto("사전 질문");
            final String requestContent = objectMapper.writeValueAsString(preQuestionDto);

            doThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다.", "작성한 사전 질문이 존재하지 않습니다."))
                    .when(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString()
                    .contains("작성한 사전 질문이 존재하지 않습니다.");

            // docs
            perform.andDo(document("pre-question/update/exception/notfound"));
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("url에서 잘못된 레벨로그 id를 입력하면 예외를 던진다.")
        void preQuestionWrongLevellogId_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            doThrow(new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .when(preQuestionService)
                    .findById(1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(get("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString()
                    .contains("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1");

            // docs
            perform.andDo(document("pre-question/findbyid/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("없는 사전 질문을 조회하면 예외를 던진다.")
        void preQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            doThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다.", "작성한 사전 질문이 존재하지 않습니다."))
                    .when(preQuestionService)
                    .findById(1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(get("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString()
                    .contains("작성한 사전 질문이 존재하지 않습니다.");

            // docs
            perform.andDo(document("pre-question/findbyid/exception/notfound"));
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("url에서 잘못된 레벨로그 id를 입력하면 예외를 던진다.")
        void preQuestionWrongLevellogId_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            doThrow(new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .when(preQuestionService)
                    .deleteById(1L, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(delete("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString()
                    .contains("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1");

            // docs
            perform.andDo(document("pre-question/delete/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("없는 사전 질문을 삭제하면 예외를 던진다.")
        void preQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            doThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다.", "작성한 사전 질문이 존재하지 않습니다."))
                    .when(preQuestionService)
                    .deleteById(1L, 1L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(delete("/api/levellogs/1/pre-questions/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString()
                    .contains("작성한 사전 질문이 존재하지 않습니다.");

            // docs
            perform.andDo(document("pre-question/delete/exception/notfound"));
        }
    }
}
