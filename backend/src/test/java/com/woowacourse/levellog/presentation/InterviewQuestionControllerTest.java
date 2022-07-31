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

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interview_question.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interview_question.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("InterviewQuestionController 클래스의")
class InterviewQuestionControllerTest extends ControllerTest {

    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.Y5wT9jBcP1lvMtjRqxaF0gMNDlgY5xs8SPhBKYChRn8";

    @Nested
    @DisplayName("save 메서드는")
    class SaveTest {

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void save_contentBlank_exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            final InterviewQuestionDto request = InterviewQuestionDto.from(" ");
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/interview-questions", 1)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("interviewQuestion must not be blank"));

            // docs
            perform.andDo(document("interview-question/save/exception-contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void save_interviewQuestionInvalidLength_Exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?".repeat(255));
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InvalidFieldException("인터뷰 질문은 255자 이하여야합니다."))
                    .when(interviewQuestionService)
                    .save(request, 1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/levellogs/{levellogId}/interview-questions", 1)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 질문은 255자 이하여야합니다."));

            // docs
            perform.andDo(document("interview-question/save/exception-contents-length"));
        }

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 작성을 요청하면 예외를 던진다.")
        void save_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .save(request, invalidLevellogId, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            post("/api/levellogs/{levellogId}/interview-questions", invalidLevellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("레벨로그가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/save/exception-levellog"));
        }

        @Test
        @DisplayName("존재하지 않는 멤버가 작성한 인터뷰 질문 작성을 요청하면 예외를 던진다.")
        void save_memberNotFound_exception() throws Exception {
            // given
            final long invalidMemberId = 20000000L;
            final long levellogId = 1L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn(String.valueOf(invalidMemberId));
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new MemberNotFoundException("멤버가 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .save(request, levellogId, invalidMemberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            post("/api/levellogs/{levellogId}/interview-questions", levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("멤버가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/save/exception-member"));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAllTest {

        @Test
        @DisplayName("존재하지 않는 레벨로그 정보로 인터뷰 질문 목록 조회를 요청하면 예외를 던진다.")
        void findAll_levellogNotFound_exception() throws Exception {
            // given
            final long invalidLevellogId = 20000000L;
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new LevellogNotFoundException("레벨로그가 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .findAll(invalidLevellogId, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            get("/api/levellogs/{levellogId}/interview-questions", invalidLevellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("레벨로그가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/findAll/exception-levellog"));
        }

        @Test
        @DisplayName("존재하지 않는 멤버가 작성한 인터뷰 질문 목록 조회를 요청하면 예외를 던진다.")
        void findAll_memberNotFound_exception() throws Exception {
            // given
            final long invalidMemberId = 20000000L;
            final long levellogId = 1L;
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn(String.valueOf(invalidMemberId));
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new MemberNotFoundException("멤버가 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .findAll(levellogId, invalidMemberId);

            // when
            final ResultActions perform = mockMvc.perform(
                            get("/api/levellogs/{levellogId}/interview-questions", levellogId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("멤버가 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/findAll/exception-member"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class UpdateTest {

        @Test
        @DisplayName("인터뷰 질문이 공백인 경우 예외를 던진다.")
        void update_contentBlank_exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            final InterviewQuestionDto request = InterviewQuestionDto.from(" ");
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1L, 1L)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("interviewQuestion must not be blank"));

            // docs
            perform.andDo(document("interview-question/update/exception-contents-blank"));
        }

        @Test
        @DisplayName("인터뷰 질문으로 255자를 초과하는 경우 예외를 던진다.")
        void update_interviewQuestionInvalidLength_Exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 왜 사용했나요?".repeat(255));
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InvalidFieldException("인터뷰 질문은 255자 이하여야합니다."))
                    .when(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1L, 1L)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value("인터뷰 질문은 255자 이하여야합니다."));

            // docs
            perform.andDo(document("interview-question/update/exception-contents-length"));
        }

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 수정하면 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InterviewQuestionNotFoundException("인터뷰 질문이 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .update(request, invalidInterviewQuestionId, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1,
                                    invalidInterviewQuestionId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("인터뷰 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/update/exception-interviewQuestion"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() throws Exception {
            // given
            final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");
            final String requestContent = objectMapper.writeValueAsString(request);

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new UnauthorizedException("권한이 없습니다."))
                    .when(interviewQuestionService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1L, 1L)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("interview-question/update/exception-unauthorized"));
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteByIdTest {

        @Test
        @DisplayName("존재하지 않는 인터뷰 질문을 수정하면 예외를 던진다.")
        void deleteById_interviewQuestionNotFound_exception() throws Exception {
            // given
            final Long invalidInterviewQuestionId = 1000L;

            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new InterviewQuestionNotFoundException("인터뷰 질문이 존재하지 않습니다."))
                    .when(interviewQuestionService)
                    .deleteById(invalidInterviewQuestionId, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1,
                                    invalidInterviewQuestionId)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("인터뷰 질문이 존재하지 않습니다."));

            // docs
            perform.andDo(document("interview-question/deleteById/exception-interviewQuestion"));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() throws Exception {
            // given
            given(jwtTokenProvider.getPayload(ACCESS_TOKEN)).willReturn("1");
            given(jwtTokenProvider.validateToken(ACCESS_TOKEN)).willReturn(true);
            doThrow(new UnauthorizedException("권한이 없습니다."))
                    .when(interviewQuestionService)
                    .deleteById(1L, 1L);

            // when
            final ResultActions perform = mockMvc.perform(
                            delete("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}", 1L, 1L)
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("interview-question/deleteById/exception-unauthorized"));
        }
    }
}
