package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
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
        final RestAssuredResponse pepperResponse = login("페퍼");
        final RestAssuredResponse eveResponse = login("이브");
        final String pepperToken = pepperResponse.getToken();
        final String eveToken = eveResponse.getToken();
        final Long eveMemberId = eveResponse.getMemberId();

        final String teamId = requestCreateTeam("잠실 제이슨조", pepperToken, 1, List.of(eveMemberId)).getTeamId();
        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final PreQuestionDto request = PreQuestionDto.from("이브가 쓴 사전 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + eveToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/create"))
                .when()
                .post("/api/levellogs/" + levellogId + "/pre-questions")
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
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse eveResponse = login("이브");
        final String eveToken = eveResponse.getToken();
        final Long eveMemberId = eveResponse.getMemberId();

        final String teamId = requestCreateTeam("잠실 제이슨조", pepperToken, 1, List.of(eveMemberId)).getTeamId();
        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final PreQuestionDto saveRequestDto = PreQuestionDto.from("이브가 쓴 사전 질문");
        final String baseUrl = "/api/levellogs/" + levellogId + "/pre-questions/";
        final String preQuestionId = post(baseUrl, eveToken, saveRequestDto)
                .getPreQuestionId();

        final PreQuestionDto request = PreQuestionDto.from("이브가 수정한 사전 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + eveToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/update"))
                .when()
                .put(baseUrl + preQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        get(baseUrl + "my", eveToken).getResponse()
                .body("preQuestion", equalTo("이브가 수정한 사전 질문"));
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
        final RestAssuredResponse pepperResponse = login("페퍼");
        final RestAssuredResponse eveResponse = login("이브");
        final String pepperToken = pepperResponse.getToken();
        final String eveToken = eveResponse.getToken();
        final Long eveMemberId = eveResponse.getMemberId();

        final String teamId = requestCreateTeam("잠실 제이슨조", pepperToken, 1, List.of(eveMemberId)).getTeamId();
        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final PreQuestionDto saveRequestDto = PreQuestionDto.from("이브가 쓴 사전 질문");
        final String baseUrl = "/api/levellogs/" + levellogId + "/pre-questions/";
        post(baseUrl, eveToken, saveRequestDto);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + eveToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/find-my"))
                .when()
                .get(baseUrl + "my")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("preQuestion", equalTo("이브가 쓴 사전 질문"));
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
        final RestAssuredResponse pepperResponse = login("페퍼");
        final RestAssuredResponse eveResponse = login("이브");
        final String pepperToken = pepperResponse.getToken();
        final String eveToken = eveResponse.getToken();
        final Long eveMemberId = eveResponse.getMemberId();

        final String teamId = requestCreateTeam("잠실 제이슨조", pepperToken, 1, List.of(eveMemberId)).getTeamId();
        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final PreQuestionDto saveRequestDto = PreQuestionDto.from("이브가 쓴 사전 질문");
        final String baseUrl = "/api/levellogs/" + levellogId + "/pre-questions/";
        final String preQuestionId = post(baseUrl, eveToken, saveRequestDto)
                .getPreQuestionId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + eveToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/delete"))
                .when()
                .delete(baseUrl + preQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        get(baseUrl + "my", eveToken).getResponse()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
