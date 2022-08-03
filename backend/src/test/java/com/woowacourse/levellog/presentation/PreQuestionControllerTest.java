package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
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
        void save_PreQuestionNullAndBlank_Exception(final String preQuestion) throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            // when
            final ResultActions perform = requestPost("/api/levellogs/1/pre-questions", TOKEN, preQuestionDto);

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
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            BDDMockito.willThrow(new UnauthorizedException("같은 팀에 속한 멤버만 사전 질문을 작성할 수 있습니다."))
                    .given(preQuestionService)
                    .save(preQuestionDto, 1L, 4L);

            // when
            final ResultActions perform = requestPost("/api/levellogs/1/pre-questions", TOKEN, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("pre-question/create/exception/not-participant"));
        }

        @Test
        @DisplayName("내 레벨로그에 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_LevellogIsMine_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            BDDMockito.willThrow(new InvalidPreQuestionException(" [levellogId : 1]", "자기 자신에게 사전 질문을 등록할 수 없습니다."))
                    .given(preQuestionService)
                    .save(preQuestionDto, 1L, 4L);

            // when
            final ResultActions perform = requestPost("/api/levellogs/1/pre-questions", TOKEN, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("자기 자신에게 사전 질문을 등록할 수 없습니다."));

            // docs
            perform.andDo(document("pre-question/create/exception/levellog-is-mine"));
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
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            // when
            final ResultActions perform = requestPut("/api/levellogs/1/pre-questions/1", TOKEN, preQuestionDto);

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
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            BDDMockito.willThrow(
                            new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = requestPut("/api/levellogs/1/pre-questions/1", TOKEN, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message")
                            .value("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"));

            // docs
            perform.andDo(document("pre-question/update/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_PreQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            BDDMockito.willThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다."))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = requestPut("/api/levellogs/1/pre-questions/1", TOKEN, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("사전 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("pre-question/update/exception/notfound"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_FromNotMyPreQuestion_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            final PreQuestionDto preQuestionDto = PreQuestionDto.from("사전 질문");

            BDDMockito.willThrow(new UnauthorizedException("자신의 사전 질문이 아닙니다."))
                    .given(preQuestionService)
                    .update(preQuestionDto, 1L, 1L, 4L);

            // when
            final ResultActions perform = requestPut("/api/levellogs/1/pre-questions/1", TOKEN, preQuestionDto);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("pre-question/update/exception/not-my-pre-question"));
        }
    }

    @Nested
    @DisplayName("findMy 메서드는")
    class FindMy {

        @Test
        @DisplayName("타인의 사전 질문을 조회하는 경우 예외를 던진다.")
        void findMy_FromNotMyPreQuestion_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(new UnauthorizedException("자신의 사전 질문이 아닙니다."))
                    .given(preQuestionService)
                    .findMy( 1L, 4L);

            // when
            final ResultActions perform = requestGet("/api/levellogs/1/pre-questions/my", TOKEN);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("pre-question/find-my/exception/not-my-pre-question"));
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 조회하면 예외를 던진다.")
        void findMy_LevellogWrongId_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(
                            new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .given(preQuestionService)
                    .findMy(1L, 4L);

            // when
            final ResultActions perform = requestGet("/api/levellogs/1/pre-questions/my", TOKEN);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message")
                            .value("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"));

            // docs
            perform.andDo(document("pre-question/find-my/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 조회하는 경우 예외를 던진다.")
        void findMy_PreQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다."))
                    .given(preQuestionService)
                    .findMy(1L, 4L);

            // when
            final ResultActions perform = requestGet("/api/levellogs/1/pre-questions/my", TOKEN);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("사전 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("pre-question/find-my/exception/notfound"));
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 삭제하면 예외를 던진다.")
        void deleteById_LevellogWrongId_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(
                            new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 4L);

            // when
            final ResultActions perform = requestDelete("/api/levellogs/1/pre-questions/1", TOKEN);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message")
                            .value("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : 1"));

            // docs
            perform.andDo(document("pre-question/delete/exception/wrong-levellog"));
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_PreQuestionNotFound_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(new PreQuestionNotFoundException("작성한 사전 질문이 존재하지 않습니다."))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 4L);

            // when
            final ResultActions perform = requestDelete("/api/levellogs/1/pre-questions/1", TOKEN);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value("사전 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("pre-question/delete/exception/notfound"));
        }

        @Test
        @DisplayName("타인의 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_FromNotMyPreQuestion_Exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
            given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);

            BDDMockito.willThrow(new UnauthorizedException("자신의 사전 질문이 아닙니다."))
                    .given(preQuestionService)
                    .deleteById(1L, 1L, 4L);

            // when
            final ResultActions perform = requestDelete("/api/levellogs/1/pre-questions/1", TOKEN);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("pre-question/delete/exception/not-my-pre-question"));
        }
    }

    private ResultActions requestPost(final String url, final String token, final Object request)
            throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    private ResultActions requestPut(final String url, final String token, final Object request)
            throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(put(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    private ResultActions requestGet(final String url, final String token) throws Exception {
        return mockMvc.perform(get(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestDelete(final String url, final String token) throws Exception {
        return mockMvc.perform(delete(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
