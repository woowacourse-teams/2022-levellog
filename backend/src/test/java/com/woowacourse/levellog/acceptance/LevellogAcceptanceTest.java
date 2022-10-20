package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final LevellogWriteRequest request = new LevellogWriteRequest("Spring과 React를 학습했습니다.");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
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
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("Spring과 React를 학습했습니다.", teamId, PEPPER).getLevellogId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .accept(MediaType.ALL_VALUE)
                .filter(document("levellog/find"))
                .when()
                .get("/api/teams/{teamId}/levellogs/{levellogId}", teamId, levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("author.id", equalTo(PEPPER.getId().intValue()))
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
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("Spring과 React를 학습했습니다.", teamId, PEPPER).getLevellogId();

        final String updateContent = "update content";
        final LevellogWriteRequest request = new LevellogWriteRequest(updateContent);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
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
