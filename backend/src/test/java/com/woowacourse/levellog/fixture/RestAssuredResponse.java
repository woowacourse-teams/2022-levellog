package com.woowacourse.levellog.fixture;

import com.woowacourse.levellog.admin.dto.response.AdminAccessTokenResponse;
import com.woowacourse.levellog.authentication.dto.response.LoginResponse;
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

    public String getFeedbackId() {
        return response
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/feedbacks/")[1];
    }

    public Long getMemberId() {
        return response
                .extract()
                .as(LoginResponse.class)
                .getId();
    }

    public String getToken() {
        return response
                .extract()
                .as(LoginResponse.class)
                .getAccessToken();
    }

    public String getAdminToken() {
        return response
                .extract()
                .as(AdminAccessTokenResponse.class)
                .getAccessToken();
    }

    public ValidatableResponse getResponse() {
        return response;
    }
}
