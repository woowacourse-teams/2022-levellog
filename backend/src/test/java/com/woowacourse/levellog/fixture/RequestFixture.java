package com.woowacourse.levellog.fixture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import io.restassured.RestAssured;
import org.springframework.http.MediaType;

public class RequestFixture {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static LoginResponse login(final String githubId, final String nickname, final String profileUrl)
            throws JsonProcessingException {
        final GithubProfileResponse githubProfileResponse = new GithubProfileResponse(githubId, nickname, profileUrl);
        final String code = objectMapper.writeValueAsString(githubProfileResponse);

        return RestAssured.given().log().all()
                .body(new GithubCodeRequest(code))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/auth/login")
                .then().log().all()
                .extract()
                .as(LoginResponse.class);
    }
}
