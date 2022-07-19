package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("깃허브 로그인 관련 기능")
class OAuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    /*
     * Scenario: 깃허브 로그인
     *   given: 사용자가 깃허브 페이지에서 로그인했다.
     *   when: 깃허브에서 받은 코드를 이용해 레벨로그 로그인을 요청한다.
     *   then: 200 OK 상태 코드와 body에 accessToken과 profileUrl를 담아 응답받는다.
     */
    @Test
    @DisplayName("깃허브 로그인")
    void login() throws Exception {
        // given
        final String code = objectMapper.writeValueAsString(new GithubProfileResponse("11111", "test", "profile_url"));
        final GithubCodeRequest codeRequest = new GithubCodeRequest(code);

        // when
        final ValidatableResponse response = requestLogin(codeRequest);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue())
                .body("profileUrl", is("profile_url"))
                .body("id", is(1));
    }

    private ValidatableResponse requestLogin(final GithubCodeRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/auth/login")
                .then().log().all();
    }
}
