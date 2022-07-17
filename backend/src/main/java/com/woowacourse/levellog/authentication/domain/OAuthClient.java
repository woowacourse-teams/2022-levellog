package com.woowacourse.levellog.authentication.domain;

import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;

public interface OAuthClient {

    String getAccessToken(String code);

    GithubProfileResponse getProfile(String accessToken);
}
