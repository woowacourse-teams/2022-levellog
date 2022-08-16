package com.woowacourse.levellog.fixture;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.authentication.dto.LoginDto;

public enum MemberFixture {

    ROMA("로마", "101010"),
    PEPPER("페퍼", "202020"),
    ALIEN("알린", "303030"),
    RICK("릭", "404040"),
    EVE("이브", "505050"),
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
                .as(LoginDto.class)
                .getAccessToken();
    }

    public Long getId() {
        if (response == null) {
            save();
        }

        return response
                .getResponse()
                .extract()
                .as(LoginDto.class)
                .getId();
    }

    public void save() {
        response = login();
    }

    public RestAssuredResponse login() {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final GithubProfileDto response = new GithubProfileDto(githubId, nickname, nickname + ".com");
            final String code = objectMapper.writeValueAsString(response);

            return post("/api/auth/login", new GithubCodeDto(code));
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
