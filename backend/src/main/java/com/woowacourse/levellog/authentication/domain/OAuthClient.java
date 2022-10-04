package com.woowacourse.levellog.authentication.domain;

import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;

public interface OAuthClient {

    String getAccessToken(String authorizationCode);

    GithubProfileResponse getProfile(String accessToken);
}
