package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
<<<<<<< HEAD
import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.authentication.dto.LoginDto;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.member.application.MemberService;
=======
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
>>>>>>> main
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
    public LoginDto login(final GithubCodeDto request) {
        final String code = request.getAuthorizationCode();
        final String githubAccessToken = oAuthClient.getAccessToken(code);

        final GithubProfileDto githubProfile = oAuthClient.getProfile(githubAccessToken);
        final Long memberId = getMemberIdByGithubProfile(githubProfile);

        final String token = jwtTokenProvider.createToken(memberId.toString());

        return new LoginDto(memberId, token, githubProfile.getProfileUrl());
    }

    private Long getMemberIdByGithubProfile(final GithubProfileDto githubProfile) {
        final int githubId = Integer.parseInt(githubProfile.getGithubId());

        return memberService.saveIfNotExist(githubProfile, githubId);
    }
}
