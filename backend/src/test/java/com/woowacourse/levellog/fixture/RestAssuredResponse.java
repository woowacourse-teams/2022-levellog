package com.woowacourse.levellog.fixture;

import com.woowacourse.levellog.authentication.dto.LoginDto;
import io.restassured.response.ValidatableResponse;
import org.springframework.http.HttpHeaders;

public class RestAssuredResponse {

    private final ValidatableResponse response;

    public RestAssuredResponse(final ValidatableResponse response) {
        this.response = response;
    }

    public String getLevellogId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/levellogs/")[1];
    }

    public String getTeamId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/api/teams/")[1];
    }

    public String getPreQuestionId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/pre-questions/")[1];
    }

    public String getInterviewQuestionId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/interview-questions/")[1];
    }

    public Long getMemberId() {
        return response
                .extract()
                .as(LoginDto.class)
                .getId();
    }

    public String getToken() {
        return response
                .extract()
                .as(LoginDto.class)
                .getAccessToken();
    }

    public ValidatableResponse getResponse() {
        return response;
    }
}
