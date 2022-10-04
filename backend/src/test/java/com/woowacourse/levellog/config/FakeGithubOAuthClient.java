package com.woowacourse.levellog.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;

public class FakeGithubOAuthClient implements OAuthClient {

    private final ObjectMapper objectMapper;

    public FakeGithubOAuthClient(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String getAccessToken(final String authorizationCode) {
        if (!authorizationCode.isBlank()) {
            return authorizationCode;
        }
        throw new IllegalStateException("[TEST] code가 비어있습니다.");
    }

    @Override
    public GithubProfileResponse getProfile(final String accessToken) {
        try {
            return objectMapper.readValue(accessToken, GithubProfileResponse.class);
        } catch (final JsonProcessingException e) {
            throw new IllegalStateException("[TEST] 로그인 code에는 원하는 GithubProfilerResponse를 json 형식으로 보내야합니다.");
        }
    }
}
