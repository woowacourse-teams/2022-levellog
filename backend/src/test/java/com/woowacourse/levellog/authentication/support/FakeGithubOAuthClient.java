package com.woowacourse.levellog.authentication.support;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;

public class FakeGithubOAuthClient implements OAuthClient {

    @Override
    public String getAccessToken(final String code) {
        if (code.equals("github_auth_code")) {
            return "access_token";
        }

        return null;
    }

    @Override
    public GithubProfileResponse getProfile(final String accessToken) {
        if (accessToken.equals("access_token")) {
            return new GithubProfileResponse("0000000", "test", "profile_url");
        }
        return null;
    }
}
