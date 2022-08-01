package com.woowacourse.levellog.presentation;

import static org.hamcrest.Matchers.startsWith;
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
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("TeamController의")
class TeamControllerTest extends ControllerTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjU4ODkyNDI4LCJleHAiOjE2NTg5Mjg0Mjh9.G3l0GRTBXZjqYSBRggI4h56DLrBhO1cgsI0idgmeyMQ";

    private void mockLogin() {
        given(jwtTokenProvider.getPayload(TOKEN)).willReturn("4");
        given(jwtTokenProvider.validateToken(TOKEN)).willReturn(true);
    }

    private ResultActions requestPost(final String url, final String token, final Object body) throws Exception {
        final String json = objectMapper.writeValueAsString(body);

        return mockMvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.ALL)
                        .content(json))
                .andDo(print());
    }

    private ResultActions requestPut(final String url, final String token, final Object body) throws Exception {
        final String json = objectMapper.writeValueAsString(body);

        return mockMvc.perform(put(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.ALL)
                        .content(json))
                .andDo(print());
    }

    private void mockCreateTeam() {
        final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
        final TeamCreateDto createRequest = new TeamCreateDto("네오와 함께하는 레벨 인터뷰", "트랙룸", 1,
                LocalDateTime.now().plusDays(3), participantIds);
        given(teamService.save(createRequest, 4L)).willReturn(1L);
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 명으로 null이 들어오면 예외를 던진다.")
        void titleNull_Exception(final String title) throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final TeamCreateDto request = new TeamCreateDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("title must not be blank");

            // docs
            perform.andDo(document("team/create/exception/title/null-and-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final String title = "네오".repeat(128);
            final TeamCreateDto request = new TeamCreateDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            doThrow(new InvalidFieldException("잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]"))
                    .when(teamService)
                    .save(request, 4L);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 팀 이름을 입력했습니다.");

            // docs
            perform.andDo(document("team/create/exception/title/length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String place) throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final TeamCreateDto request = new TeamCreateDto("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("place must not be blank");

            // docs
            perform.andDo(document("team/create/exception/place/null-and-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final String place = "선릉".repeat(128);
            final TeamCreateDto request = new TeamCreateDto("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            doThrow(new InvalidFieldException("잘못된 장소를 입력했습니다. 입력한 장소 : [" + place + "]"))
                    .when(teamService)
                    .save(request, 4L);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 장소를 입력했습니다.");

            // docs
            perform.andDo(document("team/create/exception/place/length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", "트랙룸", 1, null, participantIds);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("startAt must not be null");

            // docs
            perform.andDo(document("team/create/exception/startat/null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void startAtPast_Exception() throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(4L, 5L));
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final TeamCreateDto request = new TeamCreateDto("네오 인터뷰", "선릉 트랙룸", 1, startAt, participantIds);

            doThrow(new InvalidFieldException("잘못된 시작 시간을 입력했습니다. 입력한 시작 시간 : [" + startAt + "]"))
                    .when(teamService)
                    .save(request, 4L);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 시작 시간을 입력했습니다.");

            // docs
            perform.andDo(document("team/create/exception/startat/past"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void participantsNull_Exception() throws Exception {
            // given
            mockLogin();
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3), null);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("participants must not be null");

            // docs
            perform.andDo(document("team/create/exception/participants/null"));
        }

        @Test
        @DisplayName("인터뷰어가 1명 미만이면 예외를 던진다.")
        void notPositiveInterviewerNumber_exceptionThrown() throws Exception {
            // given
            mockLogin();

            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(1L, 3L, 4L));
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", "트랙룸", 0, LocalDateTime.now().plusDays(3),
                    participants);

            // when
            final ResultActions perform = requestPost("/api/teams", TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("message").value(startsWith("interviewerNumber")));

            // docs
            perform.andDo(document("team/create/exception/interviewerNumber/not-positive"));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 명으로 null이 들어오면 예외를 던진다.")
        void titleNull_Exception(final String title) throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final TeamUpdateDto request = new TeamUpdateDto(title, "트랙룸", LocalDateTime.now().plusDays(3));

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("팀 이름이 null 또는 공백입니다.");

            // docs
            perform.andDo(document("team/update/exception/title/null-and-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final String title = "네오".repeat(128);
            final TeamUpdateDto request = new TeamUpdateDto(title, "트랙룸", LocalDateTime.now().plusDays(3));

            doThrow(new InvalidFieldException("잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]"))
                    .when(teamService)
                    .update(request, id, 4L);

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 팀 이름을 입력했습니다.");

            // docs
            perform.andDo(document("team/update/exception/title/length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String place) throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", place, LocalDateTime.now().plusDays(3));

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("장소가 null 또는 공백입니다.");

            // docs
            perform.andDo(document("team/update/exception/place/null-and-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final String place = "거실".repeat(128);
            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", place,
                    LocalDateTime.now().plusDays(3));

            doThrow(new InvalidFieldException("잘못된 장소를 입력했습니다. 입력한 장소 : [" + place + "]"))
                    .when(teamService)
                    .update(request, id, 4L);

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 장소를 입력했습니다.");

            // docs
            perform.andDo(document("team/update/exception/place/length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", "트랙룸", null);

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("시작 시간이 없습니다.");

            // docs
            perform.andDo(document("team/update/exception/startat/null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void startAtPast_Exception() throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final long id = 1;
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", "트랙룸", startAt);
            doThrow(new InvalidFieldException("잘못된 시작 시간을 입력했습니다. 입력한 시작 시간 : [" + startAt + "]"))
                    .when(teamService)
                    .update(request, id, 4L);

            // when
            final ResultActions perform = requestPut("/api/teams/" + id, TOKEN, request);

            // then
            perform.andExpect(status().isBadRequest())
                    .andReturn().getResponse().getContentAsString().contains("잘못된 시작 시간을 입력했습니다.");

            // docs
            perform.andDo(document("team/update/exception/startat/past"));
        }

        @Test
        @DisplayName("없는 팀을 수정하려고 하면 예외를 던진다.")
        void teamNotFound_Exception() throws Exception {
            // given
            mockLogin();
            mockCreateTeam();
            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(10));

            doThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", "팀이 존재하지 않습니다."))
                    .when(teamService)
                    .update(request, 10000000L, 4L);

            // when
            final ResultActions perform = requestPut("/api/teams/" + 10000000L, TOKEN, request);

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString().contains("팀이 존재하지 않습니다.");

            // docs
            perform.andDo(document("team/update/exception/notfound"));
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void teamNotFound_Exception() throws Exception {
            // given
            mockLogin();
            doThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", "팀이 존재하지 않습니다."))
                    .when(teamService)
                    .findByTeamIdAndMemberId(10000000L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(get("/api/teams/" + 10000000L)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.ALL))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString().contains("팀이 존재하지 않습니다.");

            // docs
            perform.andDo(document("team/find-by-id/exception/notfound"));
        }
    }

    @Nested
    @DisplayName("findMyRole 메서드는")
    class FindMyRole {

        @Test
        @DisplayName("요청한 사용자가 소속된 팀이 아니면 예외를 던진다.")
        void notMyTeam_exceptionThrown() throws Exception {
            // given
            final Long teamId = 2L;
            final Long memberId = 5L;

            mockLogin();
            given(teamService.findMyRole(teamId, memberId, 4L))
                    .willThrow(new UnauthorizedException("권한이 없습니다."));

            // when
            final ResultActions perform = mockMvc.perform(get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, memberId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .accept(MediaType.ALL))
                    .andDo(print());

            // then
            perform.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("message").value("권한이 없습니다."));

            // docs
            perform.andDo(document("team/find-my-role/exception/not-my-team"));
        }

        @Test
        @DisplayName("타겟 멤버가 팀의 참가자가 아니면 예외를 던진다.")
        void targetNotParticipant_exceptionThrown() throws Exception {
            // given
            final Long teamId = 2L;
            final Long memberId = 5L;

            mockLogin();
            given(teamService.findMyRole(teamId, memberId, 4L))
                    .willThrow(new ParticipantNotFoundException("팀에 참가자가 아닙니다."));

            // when
            final ResultActions perform = mockMvc.perform(get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, memberId)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .accept(MediaType.ALL))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andExpect(jsonPath("message").value("참가자를 찾을 수 없습니다."));

            // docs
            perform.andDo(document("team/find-my-role/exception/target-not-participant"));
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("없는 팀을 제거하려고 하면 예외를 던진다.")
        void teamNotFound_Exception() throws Exception {
            // given
            mockLogin();
            doThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", "팀이 존재하지 않습니다."))
                    .when(teamService)
                    .deleteById(10000000L, 4L);

            // when
            final ResultActions perform = mockMvc.perform(delete("/api/teams/" + 10000000L)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());

            // then
            perform.andExpect(status().isNotFound())
                    .andReturn().getResponse().getContentAsString().contains("팀이 존재하지 않습니다.");

            // docs
            perform.andDo(document("team/delete/exception/notfound"));
        }
    }
}
