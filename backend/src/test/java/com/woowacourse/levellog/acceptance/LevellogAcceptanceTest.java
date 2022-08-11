package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("레벨로그 관련 기능")
class LevellogAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 레벨로그 작성
     *   when: 레벨로그 작성을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/levellogs/{id}를 담아 응답받는다.
     */
    @Test
    @DisplayName("레벨로그 작성")
    void createLevellog() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamWriteDto teamRequest = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String teamId = post("/api/teams", loginResponse1.getToken(), teamRequest).getTeamId();
        final LevellogWriteDto request = LevellogWriteDto.from("Spring과 React를 학습했습니다.");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse1.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("levellog/save"))
                .when()
                .post("/api/teams/{teamId}/levellogs", teamId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    /*
     * Scenario: 레벨로그 상세 조회
     *   given: 레벨로그가 등록되어있다.
     *   when: 등록된 레벨로그를 조회한다.
     *   then: 200 Ok 상태 코드와 레벨로그를 응답 받는다.
     */
    @Test
    @DisplayName("레벨로그 상세 조회")
    void findLevellog() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamWriteDto teamRequest = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String teamId = post("/api/teams", loginResponse1.getToken(), teamRequest).getTeamId();
        final LevellogWriteDto request = LevellogWriteDto.from("Spring과 React를 학습했습니다.");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", loginResponse1.getToken(), request)
                .getLevellogId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .accept(MediaType.ALL_VALUE)
                .filter(document("levellog/find"))
                .when()
                .get("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("author.id", equalTo(loginResponse1.getMemberId().intValue()))
                .body("content", equalTo("Spring과 React를 학습했습니다."));
    }

    /*
     * Scenario: 레벨로그 수정
     *   given: 레벨로그가 등록되어있다.
     *   when: 레벨로그를 수정한다.
     *   then: 204 No Content 상태 코드를 응답 받는다.
     */
    @Test
    @DisplayName("레벨로그 수정")
    void updateLevellog() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamWriteDto teamRequest = new TeamWriteDto("잠실 제이슨조", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String teamId = post("/api/teams", loginResponse1.getToken(), teamRequest).getTeamId();
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", loginResponse1.getToken(),
                LevellogWriteDto.from("Spring과 React를 학습했습니다.")).getLevellogId();

        final String updateContent = "update content";
        final LevellogWriteDto request = LevellogWriteDto.from(updateContent);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse1.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("levellog/update"))
                .when()
                .put("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindLevellog(Long.parseLong(teamId), Long.parseLong(levellogId))
                .body("content", equalTo(updateContent));
    }

    private ValidatableResponse requestFindLevellog(final Long teamId, final Long levellogId) {
        return RestAssured.given().log().all()
                .accept(MediaType.ALL_VALUE)
                .when()
                .get("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                .then().log().all();
    }
}
