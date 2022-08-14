package com.woowacourse.levellog.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.exception.DuplicateParticipantsException;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("TeamController의")
class TeamControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 명으로 null이 들어오면 예외를 던진다.")
        void titleNull_Exception(final String title) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("title must not be blank")
            );

            // docs
            perform.andDo(document("team/create/exception/title/null-and-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final String title = "네오".repeat(128);
            final TeamWriteDto request = new TeamWriteDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            final String message = "잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/title/length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String place) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("place must not be blank")
            );

            // docs
            perform.andDo(document("team/create/exception/place/null-and-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final String place = "선릉".repeat(128);
            final TeamWriteDto request = new TeamWriteDto("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            final String message = "잘못된 장소를 입력했습니다. 입력한 장소 : [" + place + "]";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/place/length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 5L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, null, participantIds);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(startsWith("startAt must not be null"))
            );

            // docs
            perform.andDo(document("team/create/exception/startat/null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void startAtPast_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final TeamWriteDto request = new TeamWriteDto("네오 인터뷰", "선릉 트랙룸", 1, startAt, participantIds);

            final String message = "잘못된 시작 시간을 입력했습니다. 입력한 시작 시간 : [" + startAt + "]";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/startat/past"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 빈 리스트가 들어오면 예외를 던진다.")
        void participantsEmpty_Exception() throws Exception {
            // given
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    new ParticipantIdsDto(Collections.emptyList()));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest()
            );

            // docs
            perform.andDo(document("team/create/exception/participants/empty"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void participantsNull_Exception() throws Exception {
            // given
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3), null);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(containsString("participants must not be null"))
            );

            // docs
            perform.andDo(document("team/create/exception/participants/null"));
        }

        @Test
        @DisplayName("인터뷰어가 1명 미만이면 예외를 던진다.")
        void notPositiveInterviewerNumber_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 0, LocalDateTime.now().plusDays(3),
                    participants);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewerNumber must be greater than 0")
            );

            // docs
            perform.andDo(document("team/create/exception/interviewer-number/not-positive"));
        }

        @Test
        @DisplayName("인터뷰어 수가 참가자 수보다 많거나 같으면 예외를 던진다.")
        void interviewerMoreThanParticipant_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 3L, 4L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 4, LocalDateTime.now().plusDays(3),
                    participants);

            final String message = "참가자 수는 인터뷰어 수 보다 많아야 합니다.";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/interviewer-number/more-than-participant"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void save_duplicateParticipant_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participants);

            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new DuplicateParticipantsException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/participants/duplicate"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 호스트 Id가 들어오면 예외를 던진다.")
        void save_participantsWithHostId_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participants);

            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new DuplicateParticipantsException(message))
                    .given(teamService)
                    .save(request, 1L);

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/create/exception/participants/host"));
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
            final long id = 1;
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("title must not be blank")
            );

            // docs
            perform.andDo(document("team/update/exception/title/null-and-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void titleInvalidLength_Exception() throws Exception {
            // given
            final long id = 1;
            final String title = "네오".repeat(128);
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            final String message = "잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .update(request, id, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/title/length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void placeNull_Exception(final String place) throws Exception {
            // given
//            eam();
            final long id = 1;
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("place must not be blank")
            );

            // docs
            perform.andDo(document("team/update/exception/place/null-and-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void placeInvalidLength_Exception() throws Exception {
            // given
            final long id = 1;
            final String place = "거실".repeat(128);
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", place, 1, LocalDateTime.now().plusDays(3),
                    participantIds);

            final String message = "잘못된 장소를 입력했습니다. 입력한 장소 : [" + place + "]";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .update(request, id, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/place/length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            final long id = 1;
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, null, participantIds);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("startAt must not be null")
            );

            // docs
            perform.andDo(document("team/update/exception/startat/null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void startAtPast_Exception() throws Exception {
            // given
            final long id = 1;
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 5L));
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, startAt, participantIds);

            willThrow(new InvalidFieldException("잘못된 시작 시간을 입력했습니다. 입력한 시작 시간 : [" + startAt + "]"))
                    .given(teamService)
                    .update(request, id, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(containsString("잘못된 시작 시간을 입력했습니다."))
            );

            // docs
            perform.andDo(document("team/update/exception/startat/past"));
        }

        @Test
        @DisplayName("없는 팀을 수정하려고 하면 예외를 던진다.")
        void teamNotFound_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(10),
                    participantIds);

            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", message))
                    .given(teamService)
                    .update(request, 10000000L, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(10000000L, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(containsString(message))
            );

            // docs
            perform.andDo(document("team/update/exception/notfound"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 빈 리스트가 들어오면 예외를 던진다.")
        void participantsEmpty_Exception() throws Exception {
            // given
            final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    new ParticipantIdsDto(Collections.emptyList()));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document("team/update/exception/participants/empty"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void participantsNull_Exception() throws Exception {
            // given
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3), null);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("participants must not be null")
            );

            // docs
            perform.andDo(document("team/update/exception/participants/null"));
        }

        @Test
        @DisplayName("인터뷰어가 1명 미만이면 예외를 던진다.")
        void notPositiveInterviewerNumber_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 3L, 4L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 0, LocalDateTime.now().plusDays(3),
                    participants);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewerNumber must be greater than 0")
            );

            // docs
            perform.andDo(document("team/update/exception/interviewer-number/not-positive"));
        }

        @Test
        @DisplayName("인터뷰어 수가 참가자 수보다 많거나 같으면 예외를 던진다.")
        void interviewerMoreThanParticipant_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participants);

            final String message = "참가자 수는 인터뷰어 수 보다 많아야 합니다.";
            willThrow(new InvalidFieldException(message))
                    .given(teamService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/interviewer-number/more-than-participant"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void duplicateParticipant_exceptionThrown() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participants);
            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new DuplicateParticipantsException(message))
                    .given(teamService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/participants/duplicate"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 호스트 Id가 들어오면 예외를 던진다.")
        void participantsWithHostId_exceptionThrown() throws Exception {
            // given
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    new ParticipantIdsDto(List.of(1L, 2L, 1L)));

            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new DuplicateParticipantsException(message))
                    .given(teamService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/participants/host"));
        }

        @Test
        @DisplayName("인터뷰 시작 이후에 팀을 수정하려고 하면 예외를 던진다.")
        void updateAfterStartAt_Exception() throws Exception {
            // given
            final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(2L, 3L));
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    participants);

            final String message = "인터뷰가 시작된 이후에는 수정할 수 없습니다.";
            willThrow(new InterviewTimeException(message))
                    .given(teamService)
                    .update(request, 1L, 1L);

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/update/exception/after-start-at"));
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void teamNotFound_Exception() throws Exception {
            // given
            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", message))
                    .given(teamService)
                    .findByTeamIdAndMemberId(10000000L, 1L);

            // when
            final ResultActions perform = requestFindTeam(10000000L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

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

            final String message = "권한이 없습니다.";
            given(teamService.findMyRole(teamId, memberId, 1L))
                    .willThrow(new UnauthorizedException(message));

            // when
            final ResultActions perform = requestFindMyRole(teamId, memberId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/find-my-role/exception/not-my-team"));
        }

        @Test
        @DisplayName("타겟 멤버가 팀의 참가자가 아니면 예외를 던진다.")
        void targetNotParticipant_exceptionThrown() throws Exception {
            // given
            final Long teamId = 2L;
            final Long memberId = 5L;

            final String message = "참가자를 찾을 수 없습니다.";
            given(teamService.findMyRole(teamId, memberId, 1L))
                    .willThrow(new ParticipantNotFoundException(message));

            // when
            final ResultActions perform = requestFindMyRole(teamId, memberId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/find-my-role/exception/target-not-participant"));
        }
    }

    @Nested
    @DisplayName("close 메서드는")
    class Close {

        @Test
        @DisplayName("존재하지 않는 팀의 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_notFoundTeam_exceptionThrown() throws Exception {
            // given
            final Long teamId = 200_000L;

            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException("팀이 존재하지 않습니다. [teamId : " + teamId + "]", message))
                    .given(teamService)
                    .close(teamId, 1L);

            // when
            final ResultActions perform = requestCloseTeam(teamId);
            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/close/exception/notfound"));
        }

        @Test
        @DisplayName("이미 종료된 팀 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_alreadyClosed_exceptionThrown() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "이미 종료된 인터뷰입니다.";
            willThrow(new InterviewTimeException(message))
                    .given(teamService)
                    .close(teamId, 1L);

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/close/exception/already-close"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간 전에 종료하려고 하면 예외가 발생한다.")
        void close_beforeStart_exceptionThrown() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "인터뷰가 시작되기 전에 종료할 수 없습니다.";
            willThrow(new InterviewTimeException(message))
                    .given(teamService)
                    .close(teamId, 1L);

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/close/exception/before-start"));
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_notHost_exceptionThrown() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "호스트 권한이 없습니다.";
            willThrow(new HostUnauthorizedException(message))
                    .given(teamService)
                    .close(teamId, 1L);

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/close/exception/unauthorized"));
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("없는 팀을 제거하려고 하면 예외를 던진다.")
        void delete_teamNotFound_Exception() throws Exception {
            // given
            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [10000000]", message))
                    .given(teamService)
                    .deleteById(10000000L, 1L);

            // when
            final ResultActions perform = requestDeleteTeam(10000000L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(startsWith(message))
            );

            // docs
            perform.andDo(document("team/delete/exception/notfound"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간 후에 삭제하려고 하면 예외가 발생한다.")
        void delete_afterStart_exceptionThrown() throws Exception {
            // given
            final Long teamId = 1L;
            final String message = "인터뷰가 시작된 이후에는 삭제할 수 없습니다.";
            willThrow(new InterviewTimeException(message))
                    .given(teamService)
                    .deleteById(teamId, 1L);

            // when
            final ResultActions perform = requestDeleteTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/delete/exception/after-start"));
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 팀을 삭제하려고 하면 예외가 발생한다.")
        void delete_notHost_exceptionThrown() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "호스트 권한이 없습니다.";
            willThrow(new HostUnauthorizedException(message))
                    .given(teamService)
                    .deleteById(teamId, 1L);

            // when
            final ResultActions perform = requestDeleteTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document("team/delete/exception/unauthorized"));
        }
    }

    private ResultActions requestCreateTeam(final TeamWriteDto request) throws Exception {
        return requestPost("/api/teams", request);
    }

    private ResultActions requestFindTeam(final Long teamId) throws Exception {
        return requestGet("/api/teams/" + teamId);
    }

    private ResultActions requestUpdateTeam(final Long teamId, final TeamWriteDto request) throws Exception {
        return requestPut("/api/teams/" + teamId, request);
    }

    private ResultActions requestFindMyRole(final Long teamId, final Long memberId) throws Exception {
        return requestGet("/api/teams/" + teamId + "/members/" + memberId + "/my-role");
    }

    private ResultActions requestCloseTeam(final Long teamId) throws Exception {
        return requestPost("/api/teams/" + teamId + "/close");
    }

    private ResultActions requestDeleteTeam(Long teamId) throws Exception {
        return requestDelete("/api/teams/" + teamId);
    }
}
