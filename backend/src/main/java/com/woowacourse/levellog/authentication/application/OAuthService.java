package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.application.MemberService;
import com.woowacourse.levellog.authentication.domain.GithubOAuthClient;
import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.dto.MemberCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {

    private final MemberService memberService;

    private final GithubOAuthClient githubOAuthClient;

    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(final GithubCodeRequest codeRequest) {
        final String code = codeRequest.getAuthorizationCode();
        final String githubAccessToken = githubOAuthClient.getGithubAccessToken(code);
        final GithubProfileResponse githubProfile = githubOAuthClient.getGithubProfile(githubAccessToken);
        final int githubId = Integer.parseInt(githubProfile.getGithubId());

        final Long memberId = memberService.findByGithubId(githubId)
                .map(Member::getId)
                .orElseGet(() -> memberService.save(
                        new MemberCreateDto(githubProfile.getNickname(), githubId, githubProfile.getProfileUrl())));

        final String token = jwtTokenProvider.createToken(memberId.toString());

        return new LoginResponse(token, githubProfile.getProfileUrl());
    }
}
