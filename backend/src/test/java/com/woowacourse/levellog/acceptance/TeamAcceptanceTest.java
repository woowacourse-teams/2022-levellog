package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("팀 관련 기능")
class TeamAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 레벨 인터뷰 팀 생성하기
     *   given: 페퍼, 이브, 릭이 등록되어 있고, 페퍼가 로그인 상태이다.
     *   when: 팀을 생성을 요청한다.
     *   when: 인터뷰어는 1명이다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/teams/{id}를 담아 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 생성하기")
    void createTeam() {
        // given
        final String pepperToken = login("페퍼").getToken();
        final Long eveId = login("이브").getMemberId();
        final Long rickId = login("릭").getMemberId();

        final List<Long> participantIds = List.of(eveId, rickId);

        final TeamCreateDto request = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(participantIds));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pepperToken).body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/create")).when().post("/api/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value()).header(HttpHeaders.LOCATION, equalTo("/api/teams/1"));
    }

    /*
     * Scenario: 레벨 인터뷰 팀 목록 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 팀을 목록 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 모든 팀 목록을 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 목록 조회하기")
    void findAllTeam() {
        // given
        final RestAssuredResponse pepper = login("페퍼");
        final RestAssuredResponse eve = login("이브");
        final RestAssuredResponse rick = login("릭");

        final TeamCreateDto teamCreateDto1 = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(eve.getMemberId())));
        final TeamCreateDto teamCreateDto2 = new TeamCreateDto("잠실 브리조", "톱오브스윙방", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(pepper.getMemberId(), rick.getMemberId())));

        post("/api/teams", pepper.getToken(), teamCreateDto1);
        post("/api/teams", eve.getToken(), teamCreateDto2);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/findAll")).when().get("/api/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value()).body("teams.title", contains("잠실 제이슨조", "잠실 브리조"), "teams.hostId",
                contains(pepper.getMemberId().intValue(), eve.getMemberId().intValue()), "teams.isClosed",
                contains(false, false), "teams.participants.nickname",
                contains(List.of("페퍼", "이브"), List.of("이브", "페퍼", "릭")));
    }

    /*
     * Scenario: 내가 속한 레벨 인터뷰 팀 상세 조회하기
     *   given: 팀이 등록되어 있다.
     *   given: 페퍼의 계정으로 로그인되어있다.
     *   when: 페퍼가 참여한 팀 상세 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 팀을 응답받는다.
     */
    @Test
    @DisplayName("나의 팀 상세 조회하기")
    void findTeam_myTeam() {
        // given
        final RestAssuredResponse pepper = login("페퍼");

        final Long eveId = login("이브").getMemberId();
        final Long rickId = login("릭").getMemberId();
        final Long romaId = login("로마").getMemberId();

        final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(eveId, rickId, romaId));
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 2, LocalDateTime.now().plusDays(3),
                participants);

        final String id = post("/api/teams", pepper.getToken(), teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pepper.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/find/my-team")).when()
                .get("/api/teams/{id}", id).then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value()).body("title", equalTo("잠실 제이슨조"), "place", equalTo("트랙룸"), "hostId",
                equalTo(pepper.getMemberId().intValue()), "isClosed", equalTo(false), "participants.nickname",
                contains("페퍼", "이브", "릭", "로마"), "interviewers", contains(eveId.intValue(), rickId.intValue()),
                "interviewees", contains(rickId.intValue(), romaId.intValue()));
    }

    /*
     * Scenario: 내가 속하지 않은 레벨 인터뷰 팀 상세 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 로그인하지 않고 팀 상세 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 팀을 응답받는다.
     */
    @Test
    @DisplayName("로그인하지 않고 팀 상세 조회하기")
    void findTeam_notLogin() {
        // given
        final RestAssuredResponse pepper = login("페퍼");

        final Long eveId = login("이브").getMemberId();
        final Long rickId = login("릭").getMemberId();
        final Long romaId = login("로마").getMemberId();

        final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(eveId, rickId, romaId));
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 2, LocalDateTime.now().plusDays(3),
                participants);

        final String id = post("/api/teams", pepper.getToken(), teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/find/not-my-team")).when()
                .get("/api/teams/{id}", id).then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value()).body("title", equalTo("잠실 제이슨조"), "place", equalTo("트랙룸"), "hostId",
                equalTo(pepper.getMemberId().intValue()), "participants.nickname", contains("페퍼", "이브", "릭", "로마"),
                "interviewers", empty(), "interviewees", empty());
    }

    /*
     * Scenario: 팀 참가자에 대한 나(페퍼)의 역할 조회하기
     *   given: 팀이 등록되어 있다.
     *   given: 페퍼 계정으로 로그인되어 있다.
     *   when: 나(페퍼)는 같은 팀의 참가자안 릭에 대한 나의 역할을 조회한다.
     *   then: 200 Ok 상태 코드와 interviewer를 응답 받는다.
     */
    @Test
    @DisplayName("같은 팀 참가자에 대한 나의 역할 조회하기 - interviewer")
    void findMyRole_interviewer() {
        // given
        final String pepperToken = login("페퍼").getToken();

        final Long eveId = login("이브").getMemberId();
        final Long rickId = login("릭").getMemberId();
        final Long romaId = login("로마").getMemberId();

        final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(eveId, rickId, romaId));
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 2, LocalDateTime.now().plusDays(3),
                participants);

        final String teamId = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .headers(HttpHeaders.AUTHORIZATION, "Bearer " + pepperToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/find-my-role/interviewer")).when()
                .get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, rickId).then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value()).body("myRole", equalTo("INTERVIEWER"));
    }

    /*
     * Scenario: 팀 참가자에 대한 나의 역할 조회하기
     *   given: 팀이 등록되어 있다.
     *   given: 페퍼 계정으로 로그인되어 있다.
     *   when: 나(페퍼)는 같은 팀의 참가자안 이브에 대한 나의 역할을 조회한다.
     *   then: 200 Ok 상태 코드와 observer를 응답 받는다.
     */
    @Test
    @DisplayName("같은 팀 참가자에 대한 나의 역할 조회하기 - observer")
    void findMyRole_observer() {
        // given
        final String pepperToken = login("페퍼").getToken();

        final Long eveId = login("이브").getMemberId();
        final Long rickId = login("릭").getMemberId();
        final Long romaId = login("로마").getMemberId();

        final ParticipantIdsDto participants = new ParticipantIdsDto(List.of(eveId, rickId, romaId));
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 2, LocalDateTime.now().plusDays(3),
                participants);

        final String teamId = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .headers(HttpHeaders.AUTHORIZATION, "Bearer " + pepperToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/find-my-role/observer")).when()
                .get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, eveId).then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value()).body("myRole", equalTo("OBSERVER"));
    }

    /*
     * Scenario: 레벨 인터뷰 종료하기
     *   given: 팀이 등록되어 있다.
     *   when: 등록된 팀에 대한 인터뷰 종료를 요청한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 종료하기")
    void closeInterview() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));

        final String id = post("/api/teams", loginResponse1.getToken(), teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse1.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/close")).when()
                .post("/api/teams/{id}/close", id).then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());

        final ValidatableResponse teamFindResponse = get("/api/teams/" + id).getResponse();
        teamFindResponse.body("isClosed", equalTo(true));
    }

    /*
     * Scenario: 레벨 인터뷰 팀 정보 수정하기
     *   given: 팀이 등록되어 있다.
     *   when: 팀 정보 수정을 요청한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 정보 수정하기")
    void update() {
        // given
        final String pepperToken = login("페퍼").getToken();
        final Long eveId = login("이브").getMemberId();

        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(eveId)));
        final String id = post("/api/teams", pepperToken, teamCreateDto).getTeamId();
        final TeamUpdateDto request = new TeamUpdateDto("선릉 브리조", "수성방", LocalDateTime.now().plusDays(3));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pepperToken).body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/update")).when()
                .put("/api/teams/{id}", id).then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    /*
     * Scenario: 레벨 인터뷰 팀 삭제하기
     *   given: 팀이 등록되어 있다.
     *   when: 등록된 팀을 삭제한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 삭제하기")
    void delete() {
        // given
        final String pepperToken = login("페퍼").getToken();
        final Long eveId = login("이브").getMemberId();

        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(eveId)));
        final String id = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + pepperToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/delete")).when()
                .delete("/api/teams/{id}", id).then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
