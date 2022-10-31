package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthClient oAuthClient;

    public GithubProfileResponse requestGithubProfile(final GithubCodeRequest request) {
        final String code = request.getAuthorizationCode();
        final String githubAccessToken = oAuthClient.getAccessToken(code);

        return oAuthClient.getProfile(githubAccessToken);
    }
}
