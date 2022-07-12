package com.woowacourse.levellog.acceptance;

import com.woowacourse.levellog.dto.LevellogCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
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
        final ValidatableResponse response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/levellogs")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, Matchers.equalTo("/api/levellogs/1"));
    }
}
