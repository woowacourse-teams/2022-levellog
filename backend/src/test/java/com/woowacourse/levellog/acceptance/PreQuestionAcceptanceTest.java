package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.MemberFixture;
import com.woowacourse.levellog.prequestion.dto.request.PreQuestionWriteRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("사전 질문 관련 기능")
class PreQuestionAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 사전 질문 등록하기
     *   given: 1개의 레벨로그와 2명의 사용자(페퍼, 이브)가 있다.
     *   when: 이브가 페퍼의 레벨로그에 사전 질문 등록을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/levellogs/{levellogsId}/pre-question/{preQuestionId}를 담아 응답받는다.
     */
    @Test
    @DisplayName("사전 질문 등록하기")
    void create() {
        // given
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        final PreQuestionWriteRequest request = new PreQuestionWriteRequest("이브가 쓴 사전 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + EVE.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/create"))
                .when()
                .post("/api/levellogs/{levellogId}/pre-questions", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    /*
     * Scenario: 사전 질문 수정하기
     *   given: 1개의 레벨로그와 2명의 사용자(페퍼, 이브)가 있다.
     *   given: 이브가 페퍼에게 작성한 사전 질문이 있다.
     *   when: 이브가 페퍼의 레벨로그에 쓴 사전 질문 수정을 요청한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("사전 질문 수정하기")
    void update() {
        // given
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        final String preQuestionId = savePreQuestion("이브가 쓴 사전 질문", levellogId, EVE).getPreQuestionId();

        final PreQuestionWriteRequest request = new PreQuestionWriteRequest("이브가 수정한 사전 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + EVE.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/update"))
                .when()
                .put("/api/levellogs/{levellogId}/pre-questions/{preQuestionId}", levellogId, preQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindMyPreQuestions(levellogId, EVE)
                .body("content", equalTo("이브가 수정한 사전 질문"));
    }

    /*
     * Scenario: 내가 작성한 사전 질문 조회하기
     *   given: 1개의 레벨로그와 2명의 사용자(페퍼, 이브)가 있다.
     *   given: 이브가 페퍼에게 작성한 사전 질문이 있다.
     *   when: 이브가 페퍼의 레벨로그에 쓴 사전 질문 조회를 요청한다.
     *   then: 200 ok 상태 코드와 사전 질문 데이터를 응답받는다.
     */
    @Test
    @DisplayName("내가 작성한 사전 질문 조회하기")
    void findMy() {
        // given
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        savePreQuestion("이브가 쓴 사전 질문", levellogId, EVE);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + EVE.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/find-my"))
                .when()
                .get("/api/levellogs/{levellogId}/pre-questions/my", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("content", equalTo("이브가 쓴 사전 질문"),
                        "author.id", equalTo(EVE.getId().intValue()));
    }

    /*
     * Scenario: 사전 질문 제거하기
     *   given: 1개의 레벨로그와 2명의 사용자(페퍼, 이브)가 있다.
     *   given: 이브가 페퍼에게 작성한 사전 질문이 있다.
     *   when: 이브가 페퍼의 레벨로그에 쓴 사전 질문 삭제를 요청한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("사전 질문 제거하기")
    void delete() {
        // given
        PEPPER.save();
        EVE.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();
        final String preQuestionId = savePreQuestion("이브가 쓴 사전 질문", levellogId, EVE).getPreQuestionId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + EVE.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/pre-questions/{preQuestionId}", levellogId, preQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindMyPreQuestions(levellogId, EVE)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private ValidatableResponse requestFindMyPreQuestions(final String levellogId, final MemberFixture member) {
        return get("/api/levellogs/" + levellogId + "/pre-questions/my", member.getToken()).getResponse();
    }
}
