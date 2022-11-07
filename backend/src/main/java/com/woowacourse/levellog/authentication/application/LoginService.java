package com.woowacourse.levellog.authentication.application;

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
public class LoginService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public LoginResponse login(final GithubProfileResponse githubProfile) {
        final Long memberId = getMemberIdByGithubProfile(githubProfile);
        final String token = jwtTokenProvider.createToken(memberId.toString());

        return new LoginResponse(memberId, token, githubProfile.getNickname(), githubProfile.getProfileUrl());
    }

    private Long getMemberIdByGithubProfile(final GithubProfileResponse githubProfile) {
        return memberService.saveIfNotExist(githubProfile);
    }
}
