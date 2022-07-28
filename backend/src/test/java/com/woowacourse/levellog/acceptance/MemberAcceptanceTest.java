package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayName("멤버 관련 기능")
public class MemberAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 멤버 검색
     *   given: 가입된 회원들이 있다.
     *   when: 특정 닉네임 키워드로 멤버 검색 요청을 보낸다.
     *   then: 특정 닉네임 키워드가 포함된 멤버의 정보와 200 OK 상태코드를 응답받는다.
     */
    @Test
    @DisplayName("멤버 검색")
    void searchByNickname() {
        // given
        final String eveToken = login("eve1").getToken();
        login("eve2");
        login("eve3");
        login("levellog");
        login("알린");
        login("알린2");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + eveToken)
                .filter(document("member/search"))
                .when()
                .get("/api/members?nickname=" + "eve")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("members.nickname", containsInAnyOrder("eve1", "eve2", "eve3", "levellog"));
    }
}
