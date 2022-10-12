package com.woowacourse.levellog.presentation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.dto.request.TeamWriteRequest;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
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

    @Test
    @DisplayName("findAll 메서드는 status로 잘못된 값을 입력 받으면 예외가 발생한다.")
    void findAll_invalidStatus_exception() throws Exception {
        // given
        final String invalidStatus = "invalid";
        final String message = "입력 받은 필터링 조건이 올바르지 않습니다.";

        // when
        final ResultActions perform = requestGet("/api/teams?condition=" + invalidStatus);

        // then
        perform.andExpectAll(
                status().isBadRequest(),
                jsonPath("message").value(message)
        );

        // docs
        perform.andDo(document("team/find-all/exception/invalid-status"));
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        private static final String BASE_SNIPPET_PATH = "team/create/exception/";

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 명으로 null이 들어오면 예외를 던진다.")
        void save_titleNull_exception(final String title) throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("title must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "title-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void save_titleInvalidLength_exception() throws Exception {
            // given
            final String title = "네오".repeat(128);
            final TeamWriteRequest request = new TeamWriteRequest(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "팀 이름은 255 이하여야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "title-length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void save_placeNull_exception(final String place) throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("place must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "place-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void save_placeInvalidLength_exception() throws Exception {
            // given
            final String place = "선릉".repeat(128);
            final TeamWriteRequest request = new TeamWriteRequest("네오 인터뷰", place, 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "장소 이름은 255 이하여야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "place-length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void save_startAtNull_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, null, List.of(1L, 5L),
                    Collections.emptyList());

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(startsWith("startAt must not be null"))
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "start-at-null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void save_startAtPast_exception() throws Exception {
            // given
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final TeamWriteRequest request = new TeamWriteRequest("네오 인터뷰", "선릉 트랙룸", 1, startAt, List.of(2L, 3L),
                    Collections.emptyList());

            final String message = "인터뷰 시작 시간은 현재 시간 이후여야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "start-at-past"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 빈 리스트가 들어오면 예외를 던진다.")
        void save_participantsEmpty_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    Collections.emptyList(), List.of(1L, 2L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest()
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-empty"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void save_participantsNull_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    null, List.of(1L, 2L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(containsString("participantIds must not be empty"))
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-null"));
        }

        @Test
        @DisplayName("인터뷰어가 1명 미만이면 예외를 던진다.")
        void save_notPositiveInterviewerNumber_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 0, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewerNumber must be greater than 0")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "interviewer-number-not-positive"));
        }

        @Test
        @DisplayName("인터뷰어 수가 참가자 수보다 많거나 같으면 예외를 던진다.")
        void save_interviewerMoreThanParticipant_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 4, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L, 4L), Collections.emptyList());

            final String message = "참가자 수는 인터뷰어 수 보다 많아야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "interviewer-number-more-than-participant"));
        }

        @Test
        @DisplayName("팀 참가자 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void save_duplicateParticipant_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 2L, 3L), Collections.emptyList());

            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-duplicate"));
        }

        @Test
        @DisplayName("팀 참관자 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void save_duplicateWatcher_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(1L, 2L, 3L), List.of(4L, 4L, 5L));

            final String message = "중복되는 참관자가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "watchers-duplicate"));
        }

        @Test
        @DisplayName("팀 참가자와 참관자 목록에 겹치는 Id가 들어오면 예외를 던진다.")
        void save_notIndependent_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(1L, 2L, 3L, 4L), List.of(4L, 5L));

            final String message = "참가자와 참관자에 모두 포함된 멤버가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-independent"));
        }

        @Test
        @DisplayName("호스트가 참가자 또는 참관자에 포함되지 않으면 예외가 던진다.")
        void save_hostExistence_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L, 4L), List.of(5L));
            final String message = "호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .save(request, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCreateTeam(request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "host-existence"));
        }

        private ResultActions requestCreateTeam(final TeamWriteRequest request) throws Exception {
            return requestPost("/api/teams", request);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        private static final String BASE_SNIPPET_PATH = "team/update/exception/";

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("팀 명으로 null이 들어오면 예외를 던진다.")
        void update_titleNull_exception(final String title) throws Exception {
            // given
            final long id = 1;
            final TeamWriteRequest request = new TeamWriteRequest(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("title must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "title-blank"));
        }

        @Test
        @DisplayName("팀 명으로 255자를 초과할 경우 예외를 던진다.")
        void update_titleInvalidLength_exception() throws Exception {
            // given
            final long id = 1;
            final String title = "네오".repeat(128);
            final TeamWriteRequest request = new TeamWriteRequest(title, "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "팀 이름은 255 이하여야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, id, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "title-length"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("장소로 null이 들어오면 예외를 던진다.")
        void update_placeNull_exception(final String place) throws Exception {
            // given
            final long id = 1;
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", place, 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("place must not be blank")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "place-blank"));
        }

        @Test
        @DisplayName("장소로 255자를 초과할 경우 예외를 던진다.")
        void update_placeInvalidLength_exception() throws Exception {
            // given
            final long id = 1;
            final String place = "거실".repeat(128);
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", place, 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "장소 이름은 255 이하여야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, id, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "place-length"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void update_startAtNull_exception() throws Exception {
            // given
            final long id = 1;
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", "트랙룸", 1, null, List.of(2L, 3L),
                    Collections.emptyList());

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("startAt must not be null")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "start-at-null"));
        }

        @Test
        @DisplayName("인터뷰 시작 시간이 현재 시간 기준으로 과거면 예외를 던진다.")
        void update_startAtPast_exception() throws Exception {
            // given
            final long id = 1;
            final LocalDateTime startAt = LocalDateTime.now().minusDays(3);
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", "트랙룸", 1, startAt, List.of(1L, 5L),
                    Collections.emptyList());

            willThrow(new InvalidFieldException("인터뷰 시작 시간은 현재 시간 이후여야 합니다.", DebugMessage.init()))
                    .given(teamService)
                    .update(request, id, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(id, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(containsString("인터뷰 시작 시간은 현재 시간 이후여야 합니다."))
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "start-at-past"));
        }

        @Test
        @DisplayName("없는 팀을 수정하려고 하면 예외를 던진다.")
        void update_teamNotFound_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(10),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException(DebugMessage.init()))
                    .given(teamService)
                    .update(request, 10000000L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(10000000L, request);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(containsString(message))
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 빈 리스트가 들어오면 예외를 던진다.")
        void update_participantsEmpty_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    Collections.emptyList(), List.of(1L, 2L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpect(status().isBadRequest());

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-empty"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void update_participantsNull_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    null,
                    List.of(1L, 2L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("participantIds must not be empty")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-null"));
        }

        @Test
        @DisplayName("인터뷰어가 1명 미만이면 예외를 던진다.")
        void update_notPositiveInterviewerNumber_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 0, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L, 4L), Collections.emptyList());

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value("interviewerNumber must be greater than 0")
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "interviewer-number-not-positive"));
        }

        @Test
        @DisplayName("인터뷰어 수가 참가자 수보다 많거나 같으면 예외를 던진다.")
        void update_interviewerMoreThanParticipant_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "참가자 수는 인터뷰어 수 보다 많아야 합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "interviewer-number-more-than-participant"));
        }

        @Test
        @DisplayName("팀 구성원 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void update_duplicateParticipant_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 2L, 3L), Collections.emptyList());
            final String message = "중복되는 참가자가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "participants-duplicate"));
        }

        @Test
        @DisplayName("팀 참관자 목록으로 중복된 Id가 들어오면 예외를 던진다.")
        void update_duplicateWatcher_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(1L, 2L, 3L), List.of(4L, 4L, 5L));
            final String message = "중복된 참관자가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "watchers-duplicate"));
        }

        @Test
        @DisplayName("팀 참가자와 참관자 목록에 겹치는 Id가 들어오면 예외를 던진다.")
        void update_notIndependent_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(1L, 2L, 3L, 4L), List.of(4L, 5L));
            final String message = "참가자와 참관자에 모두 포함된 멤버가 존재합니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-independent"));
        }

        @Test
        @DisplayName("호스트가 참가자 또는 참관자에 포함되지 않으면 예외가 던진다.")
        void update_hostExistence_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L, 4L), List.of(5L));
            final String message = "호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.";
            willThrow(new InvalidFieldException(message, DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "host-existence"));
        }

        @Test
        @DisplayName("인터뷰 시작 이후에 팀을 수정하려고 하면 예외를 던진다.")
        void update_updateAfterStartAt_exception() throws Exception {
            // given
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                    List.of(2L, 3L), Collections.emptyList());

            final String message = "인터뷰 준비 상태가 아닙니다.";
            willThrow(new TeamNotReadyException(DebugMessage.init()))
                    .given(teamService)
                    .update(request, 1L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestUpdateTeam(1L, request);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-start-at"));
        }

        private ResultActions requestUpdateTeam(final Long teamId, final TeamWriteRequest request) throws Exception {
            return requestPut("/api/teams/" + teamId, request);
        }
    }

    @Nested
    @DisplayName("findByTeamIdAndMemberId 메서드는")
    class FindByTeamIdAndMemberId {

        private static final String BASE_SNIPPET_PATH = "team/find-by-id/exception/";

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void findByTeamIdAndMemberId_teamNotFound_exception() throws Exception {
            // given
            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException(DebugMessage.init()))
                    .given(teamQueryService)
                    .findByTeamIdAndMemberId(10000000L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestFindTeam(10000000L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "notfound"));
        }

        private ResultActions requestFindTeam(final Long teamId) throws Exception {
            return requestGet("/api/teams/" + teamId);
        }
    }

    @Nested
    @DisplayName("findStatus 메서드는")
    class FindStatus {

        private static final String BASE_SNIPPET_PATH = "team/find-status/exception/";

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void findStatus_teamNotFound_Exception() throws Exception {
            // given
            final String message = "팀이 존재하지 않습니다.";
            final long teamId = 10000000L;
            willThrow(new TeamNotFoundException(DebugMessage.init()))
                    .given(teamService)
                    .findStatus(teamId);

            // when
            final ResultActions perform = requestFindStatus(teamId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "team-not-found"));
        }

        private ResultActions requestFindStatus(final Long teamId) throws Exception {
            return requestGet("/api/teams/" + teamId + "/status");
        }
    }

    @Nested
    @DisplayName("findMyRole 메서드는")
    class FindMyRole {

        private static final String BASE_SNIPPET_PATH = "team/find-my-role/exception/";

        @Test
        @DisplayName("요청한 사용자가 소속된 팀이 아니면 예외를 던진다.")
        void findMyRole_notMyTeam_exception() throws Exception {
            // given
            final Long teamId = 2L;
            final Long memberId = 5L;

            final String message = "같은 팀에 속해있지 않습니다.";
            given(teamService.findMyRole(teamId, memberId, LoginStatus.fromLogin(1L)))
                    .willThrow(new ParticipantNotSameTeamException(DebugMessage.init()));

            // when
            final ResultActions perform = requestFindMyRole(teamId, memberId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-my-team"));
        }

        @Test
        @DisplayName("타겟 멤버가 팀의 참가자가 아니면 예외를 던진다.")
        void findMyRole_targetNotParticipant_exception() throws Exception {
            // given
            final Long teamId = 2L;
            final Long memberId = 5L;

            final String message = "팀 참가자가 아닙니다.";
            given(teamService.findMyRole(teamId, memberId, LoginStatus.fromLogin(1L)))
                    .willThrow(new NotParticipantException(DebugMessage.init()));

            // when
            final ResultActions perform = requestFindMyRole(teamId, memberId);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "target-not-participant"));
        }

        private ResultActions requestFindMyRole(final Long teamId, final Long memberId) throws Exception {
            return requestGet("/api/teams/" + teamId + "/members/" + memberId + "/my-role");
        }
    }

    @Nested
    @DisplayName("close 메서드는")
    class Close {

        private static final String BASE_SNIPPET_PATH = "team/close/exception/";

        @Test
        @DisplayName("존재하지 않는 팀의 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_notFoundTeam_exception() throws Exception {
            // given
            final Long teamId = 200_000L;

            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException(DebugMessage.init()))
                    .given(teamService)
                    .close(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCloseTeam(teamId);
            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "not-found"));
        }

        @Test
        @DisplayName("이미 종료된 팀 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_alreadyClosed_exception() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "이미 인터뷰가 종료된 팀입니다.";
            willThrow(new TeamAlreadyClosedException(DebugMessage.init()))
                    .given(teamService)
                    .close(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "already-close"));
        }

        @Test
        @DisplayName("팀이 진행 중이 아닐 때 종료하려고 하면 예외가 발생한다.")
        void close_notInProgress_exception() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "인터뷰 진행중인 상태가 아닙니다.";
            willThrow(new TeamNotInProgressException(DebugMessage.init()))
                    .given(teamService)
                    .close(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "before-start"));
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 인터뷰를 종료하려고 하면 예외가 발생한다.")
        void close_notHost_exception() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "호스트 권한이 없습니다.";
            willThrow(new HostUnauthorizedException(DebugMessage.init()))
                    .given(teamService)
                    .close(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestCloseTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "unauthorized"));
        }

        private ResultActions requestCloseTeam(final Long teamId) throws Exception {
            return requestPost("/api/teams/" + teamId + "/close");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        private static final String BASE_SNIPPET_PATH = "team/delete/exception/";

        @Test
        @DisplayName("없는 팀을 제거하려고 하면 예외를 던진다.")
        void deleteById_teamNotFound_exception() throws Exception {
            // given
            final String message = "팀이 존재하지 않습니다.";
            willThrow(new TeamNotFoundException(DebugMessage.init()))
                    .given(teamService)
                    .delete(10000000L, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteTeam(10000000L);

            // then
            perform.andExpectAll(
                    status().isNotFound(),
                    jsonPath("message").value(startsWith(message))
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "notfound"));
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 삭제하려고 하면 예외가 발생한다.")
        void deleteById_notReady_exception() throws Exception {
            // given
            final Long teamId = 1L;
            final String message = "인터뷰 준비 상태가 아닙니다.";
            willThrow(new TeamNotReadyException(DebugMessage.init()))
                    .given(teamService)
                    .delete(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "after-start"));
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 팀을 삭제하려고 하면 예외가 발생한다.")
        void deleteById_notHost_exception() throws Exception {
            // given
            final Long teamId = 1L;

            final String message = "호스트 권한이 없습니다.";
            willThrow(new HostUnauthorizedException(DebugMessage.init()))
                    .given(teamService)
                    .delete(teamId, LoginStatus.fromLogin(1L));

            // when
            final ResultActions perform = requestDeleteTeam(teamId);

            // then
            perform.andExpectAll(
                    status().isUnauthorized(),
                    jsonPath("message").value(message)
            );

            // docs
            perform.andDo(document(BASE_SNIPPET_PATH + "unauthorized"));
        }

        private ResultActions requestDeleteTeam(final Long teamId) throws Exception {
            return requestDelete("/api/teams/" + teamId);
        }
    }
}
