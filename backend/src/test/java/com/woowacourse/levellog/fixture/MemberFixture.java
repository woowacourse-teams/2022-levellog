package com.woowacourse.levellog.fixture;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.response.LoginResponse;

public enum MemberFixture {

    ROMA("로마", "101010"),
    PEPPER("페퍼", "202020"),
    ALIEN("알린", "303030"),
    RICK("릭", "404040"),
    EVE("이브", "505050"),
    POBI("포비", "606060"),
    ;

    private final String nickname;
    private final String githubId;
    private RestAssuredResponse response;

    MemberFixture(final String nickname, final String githubId) {
        this.nickname = nickname;
        this.githubId = githubId;
    }

    public String getToken() {
        if (response == null) {
            save();
        }

        return response
                .getResponse()
                .extract()
                .as(LoginResponse.class)
                .getAccessToken();
    }

    public Long getId() {
        if (response == null) {
            save();
        }

        return response
                .getResponse()
                .extract()
                .as(LoginResponse.class)
                .getId();
    }

    public void save() {
        response = login();
    }

    public RestAssuredResponse login() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final GithubProfileResponse response = new GithubProfileResponse(githubId, nickname, nickname + ".com");
            final String code = objectMapper.writeValueAsString(response);

            return post("/api/auth/login", new GithubCodeRequest(code));
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
