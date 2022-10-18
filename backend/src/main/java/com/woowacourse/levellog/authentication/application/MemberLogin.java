package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberLogin {

    private final OAuthService oAuthService;
    private final OAuthClient oAuthClient;

    public LoginResponse login(final GithubCodeRequest request) {
        final String code = request.getAuthorizationCode();
        final String githubAccessToken = oAuthClient.getAccessToken(code);
        final GithubProfileResponse githubProfile = oAuthClient.getProfile(githubAccessToken);

        return oAuthService.login(githubProfile);
    }
}
