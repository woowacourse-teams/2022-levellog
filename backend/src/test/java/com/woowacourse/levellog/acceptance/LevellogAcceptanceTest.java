package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.equalTo;

import com.woowacourse.levellog.dto.LevellogCreateRequest;
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
        final LevellogCreateRequest request = new LevellogCreateRequest("heloo");

        // when
        final ValidatableResponse response = requestCreateLevellog(request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, equalTo("/api/levellogs/1"));
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
        final String content = "트렌젝션에 대해 학습함.";
        final LevellogCreateRequest request = new LevellogCreateRequest(content);
        final String id = extractLevellogId(requestCreateLevellog(request));

        // when
        final ValidatableResponse response = requestFindLevellog(id);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("content", equalTo(content));
    }

    @Test
    @DisplayName("레벨로그 수정")
    void updateLevellog() {
        // given
        final ValidatableResponse createResponse = requestCreateLevellog(new LevellogCreateRequest("original content"));
        final String id = extractLevellogId(createResponse);
        final String updateContent = "update content";
        final LevellogCreateRequest request = new LevellogCreateRequest(updateContent);

        // when
        final ValidatableResponse response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/levellogs/{id}", id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindLevellog(id)
                .body("content", equalTo(updateContent));
    }

    private ValidatableResponse requestFindLevellog(final String id) {
        return RestAssured.given().log().all()
                .accept(MediaType.ALL_VALUE)
                .when()
                .get("/api/levellogs/{id}", id)
                .then().log().all();
    }

    private ValidatableResponse requestCreateLevellog(final LevellogCreateRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/levellogs")
                .then().log().all();
    }

    private String extractLevellogId(final ValidatableResponse response) {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/api/levellogs/")[1];
    }
}
