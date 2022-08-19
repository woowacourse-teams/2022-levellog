package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.MemberFixture.RICK;
import static com.woowacourse.levellog.fixture.MemberFixture.ROMA;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredTemplate;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.dto.WatcherIdsDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.Collections;
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
        PEPPER.save();
        EVE.save();
        RICK.save();

        final List<Long> participantIds = List.of(EVE.getId(), RICK.getId());
        final TeamWriteDto request = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(participantIds), new WatcherIdsDto(Collections.emptyList()));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/create"))
                .when()
                .post("/api/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, equalTo("/api/teams/1"));
    }

    /*
     * Scenario: 레벨 인터뷰 팀 전체 목록 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 팀을 목록 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 인터뷰 종료 여부, 최근 생성일 순으로 정렬된 모든 팀 목록을 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 전체 목록 조회하기")
    void findAllTeam_success() {
        // given
        PEPPER.save();
        EVE.save();
        RICK.save();

        saveTeam("잠실 제이슨조", PEPPER, 1, EVE);
        saveTeam("잠실 브리조", EVE, 1, RICK);
        final String teamId = saveTeam("잠실 네오조", RICK, 1, PEPPER).getTeamId();

        timeStandard.setInProgress();
        RestAssuredTemplate.post("/api/teams/" + teamId + "/close", RICK.getToken());

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find-all"))
                .when()
                .get("/api/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("teams.title", contains("잠실 브리조", "잠실 제이슨조", "잠실 네오조"),
                        "teams.hostId",
                        contains(EVE.getId().intValue(), PEPPER.getId().intValue(), RICK.getId().intValue()),
                        "teams.status", contains("IN_PROGRESS", "IN_PROGRESS", "CLOSED"),
                        "teams.isParticipant", contains(false, true, true),
                        "teams.participants.nickname",
                        contains(List.of("이브", "릭"), List.of("페퍼", "이브"), List.of("릭", "페퍼")));
    }

    /*
     * Scenario: 진행 상태로 필터링된 레벨 인터뷰 팀 목록 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 진행 중인 팀을 목록 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 최근 생성일 순으로 정렬된 모든 진행 중인 팀 목록을 응답받는다.
     */
    @Test
    @DisplayName("필터링된 레벨 인터뷰 팀 전체 목록 조회하기")
    void findAllTeam_filteringByTeamStatus_success() {
        // given
        PEPPER.save();
        EVE.save();
        RICK.save();

        saveTeam("잠실 제이슨조", PEPPER, 1, EVE);
        saveTeam("잠실 브리조", EVE, 1, RICK);

        timeStandard.setInProgress();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find-all/filtering-by-status"))
                .when()
                .get("/api/teams?status=in-progress")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("teams.title", contains("잠실 브리조", "잠실 제이슨조"),
                        "teams.hostId", contains(EVE.getId().intValue(), PEPPER.getId().intValue()),
                        "teams.status", contains("IN_PROGRESS", "IN_PROGRESS"),
                        "teams.isParticipant", contains(false, true),
                        "teams.participants.nickname", contains(List.of("이브", "릭"), List.of("페퍼", "이브")));
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
        PEPPER.save();
        EVE.save();
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 2, EVE, RICK, ROMA).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find/my-team"))
                .when()
                .get("/api/teams/{id}", teamId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("title", equalTo("잠실 제이슨조"),
                        "place", equalTo("잠실 제이슨조place"),
                        "hostId", equalTo(PEPPER.getId().intValue()),
                        "status", equalTo("READY"),
                        "isParticipant", equalTo(true),
                        "participants.nickname", contains("페퍼", "이브", "릭", "로마"),
                        "interviewers", contains(EVE.getId().intValue(), RICK.getId().intValue()),
                        "interviewees", contains(RICK.getId().intValue(), ROMA.getId().intValue()));
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
        PEPPER.save();
        EVE.save();
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 2, EVE, RICK, ROMA).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE).filter(document("team/find/not-my-team")).when()
                .get("/api/teams/{id}", teamId).then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("title", equalTo("잠실 제이슨조"),
                        "place", equalTo("잠실 제이슨조place"),
                        "hostId", equalTo(PEPPER.getId().intValue()),
                        "status", equalTo("READY"),
                        "isParticipant", equalTo(false),
                        "participants.nickname", contains("페퍼", "이브", "릭", "로마"),
                        "interviewers", empty(),
                        "interviewees", empty());
    }

    /*
     * Scenario: 팀 상태 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 로그인하지 않고 팀 상태 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 팀 상태를 응답받는다.
     */
    @Test
    @DisplayName("팀 상태 조회하기")
    void findStatus() {
        // given
        PEPPER.save();
        RICK.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, RICK).getTeamId();

        timeStandard.setInProgress(); // 인터뷰 시작

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find-status"))
                .when()
                .get("/api/teams/{teamId}/status", teamId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("status", equalTo(TeamStatus.IN_PROGRESS.name()));
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
        PEPPER.save();
        EVE.save();
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 2, EVE, RICK, ROMA).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .headers(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find-my-role/interviewer"))
                .when()
                .get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, RICK.getId())
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("myRole", equalTo("INTERVIEWER"));
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
        PEPPER.save();
        EVE.save();
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 2, EVE, RICK, ROMA).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .headers(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find-my-role/observer"))
                .when()
                .get("/api/teams/{teamId}/members/{memberId}/my-role", teamId, EVE.getId())
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("myRole", equalTo("OBSERVER"));
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
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, EVE).getTeamId();

        timeStandard.setInProgress();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/close"))
                .when()
                .post("/api/teams/{id}/close", teamId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());

        get("/api/teams/" + teamId).getResponse()
                .body("status", equalTo("CLOSED"));
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
        PEPPER.save();
        EVE.save();
        RICK.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, EVE).getTeamId();

        final TeamWriteDto request = new TeamWriteDto("선릉 브리조", "수성방", 2, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(EVE.getId(), RICK.getId())), new WatcherIdsDto(Collections.emptyList()));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/update"))
                .when()
                .put("/api/teams/{id}", teamId)
                .then().log().all();

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
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, EVE).getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/delete"))
                .when()
                .delete("/api/teams/{id}", teamId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
