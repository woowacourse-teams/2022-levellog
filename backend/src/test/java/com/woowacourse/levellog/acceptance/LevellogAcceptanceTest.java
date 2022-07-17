package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

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
        final Long id = extractLevellogId(requestCreateLevellog(request));

        // when
        final ValidatableResponse response = requestFindLevellog(id);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("content", equalTo(content));
    }

    /*
     * Scenario: 레벨로그 상세 조회
     *   when: 존재하지 않는 레벨로그를 조회한다.
     *   then: 500 상태 코드를 응답 받는다.
     */
    @Test
    @DisplayName("존재하지 않는 레벨로그 상세 조회")
    void findLevellog_notExistId_500() {
        // when
        final ValidatableResponse response = requestFindLevellog(999L);

        // then
        response.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        final ValidatableResponse createResponse = requestCreateLevellog(new LevellogCreateRequest("original content"));
        final Long id = extractLevellogId(createResponse);
        final String updateContent = "update content";
        final LevellogCreateRequest request = new LevellogCreateRequest(updateContent);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("levellog/update"))
                .when()
                .put("/api/levellogs/{id}", id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindLevellog(id)
                .body("content", equalTo(updateContent));
    }

    /*
     * Scenario: 레벨로그 삭제
     *   given: 레벨로그가 등록되어있다.
     *   when: 레벨로그를 삭제한다.
     *   then: 204 No Content 상태 코드를 응답 받는다.
     */
    @Test
    @DisplayName("레벨로그 삭제")
    void deleteLevellog() {
        // given
        final ValidatableResponse createResponse = requestCreateLevellog(new LevellogCreateRequest("original content"));
        final Long id = extractLevellogId(createResponse);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("levellog/delete"))
                .when()
                .delete("/api/levellogs/{id}", id)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindLevellog(id)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ValidatableResponse requestCreateLevellog(final LevellogCreateRequest request) {
        return RestAssured.given(specification).log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("levellog/create"))
                .when()
                .post("/api/levellogs")
                .then().log().all();
    }

    private Long extractLevellogId(final ValidatableResponse response) {
        return Long.valueOf(response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/api/levellogs/")[1]);
    }

    private ValidatableResponse requestFindLevellog(final Long id) {
        return RestAssured.given(specification).log().all()
                .accept(MediaType.ALL_VALUE)
                .filter(document("levellog/find"))
                .when()
                .get("/api/levellogs/{id}", id)
                .then().log().all();
    }
}
