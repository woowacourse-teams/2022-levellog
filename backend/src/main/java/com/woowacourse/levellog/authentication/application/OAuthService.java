package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.response.LoginResponse;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.member.application.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthClient oAuthClient;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public LoginResponse login(final GithubCodeRequest request) {
        final String code = request.getAuthorizationCode();
        final String githubAccessToken = oAuthClient.getAccessToken(code);

        final GithubProfileResponse githubProfile = oAuthClient.getProfile(githubAccessToken);
        final Long memberId = getMemberIdByGithubProfile(githubProfile);

        final String token = jwtTokenProvider.createToken(memberId.toString());

        return new LoginResponse(memberId, token, githubProfile.getNickname(), githubProfile.getProfileUrl());
    }

    private Long getMemberIdByGithubProfile(final GithubProfileResponse githubProfile) {
        final int githubId = Integer.parseInt(githubProfile.getGithubId());

        return memberService.saveIfNotExist(githubProfile, githubId);
    }
}
