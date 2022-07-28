package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.fixture.RestAssuredTemplate;
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
     *   given: 2명의 맴버가 등록되어 있고, 1명의 멤버가 로그인 상태이다.
     *   when: 팀을 생성을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/teams/{id}를 담아 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 생성하기")
    void createTeam() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto request = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .auth().oauth2(loginResponse1.getToken())
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
     * Scenario: 레벨 인터뷰 팀 목록 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 팀을 목록 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 모든 팀 목록을 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 목록 조회하기")
    void findAllTeam() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final RestAssuredResponse loginResponse3 = login("릭");
        final TeamCreateDto teamCreateDto1 = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final TeamCreateDto teamCreateDto2 = new TeamCreateDto("잠실 브리조", "톱오브스윙방", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(
                        List.of(loginResponse1.getMemberId(), loginResponse3.getMemberId())));

        RestAssuredTemplate.post("/api/teams", loginResponse1.getToken(), teamCreateDto1);
        RestAssuredTemplate.post("/api/teams", loginResponse2.getToken(), teamCreateDto2);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/findAll"))
                .when()
                .get("/api/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("teams.title", contains("잠실 제이슨조", "잠실 브리조"),
                        "teams.hostId", contains(loginResponse1.getMemberId().intValue(), loginResponse2.getMemberId().intValue()),
                        "teams.participants.nickname", contains(List.of("페퍼", "이브"), List.of("이브", "페퍼", "릭"))
                );
    }

    /*
     * Scenario: 레벨 인터뷰 팀 상세 조회하기
     *   given: 팀이 등록되어 있다.
     *   when: 팀 상세 조회를 요청한다.
     *   then: 200 Ok 상태 코드와 팀을 응답받는다.
     */
    @Test
    @DisplayName("레벨 인터뷰 팀 상세 조회하기")
    void findTeam() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));

        final String id = RestAssuredTemplate.post("/api/teams", loginResponse1.getToken(), teamCreateDto)
                .getTeamId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/find"))
                .when()
                .get("/api/teams/{id}", id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("title", equalTo("잠실 제이슨조"),
                        "place", equalTo("트랙룸"),
                        "hostId", equalTo(loginResponse1.getMemberId().intValue()),
                        "participants.nickname", contains("페퍼", "이브")
                );
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
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String id = RestAssuredTemplate.post("/api/teams", loginResponse1.getToken(), teamCreateDto)
                .getTeamId();
        final TeamUpdateDto request = new TeamUpdateDto("선릉 브리조", "수성방", LocalDateTime.now().plusDays(3));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .auth().oauth2(loginResponse1.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/update"))
                .when()
                .put("/api/teams/{id}", id)
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
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String id = RestAssuredTemplate.post("/api/teams", loginResponse1.getToken(), teamCreateDto)
                .getTeamId();
        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .auth().oauth2(loginResponse1.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("team/delete"))
                .when()
                .delete("/api/teams/{id}", id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
