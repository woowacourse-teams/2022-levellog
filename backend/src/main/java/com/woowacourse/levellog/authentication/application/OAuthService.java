package com.woowacourse.levellog.authentication.application;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuthService {

    private final MemberService memberService;
    private final OAuthClient oAuthClient;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(final GithubCodeRequest codeRequest) {
        final String code = codeRequest.getAuthorizationCode();
        final String githubAccessToken = oAuthClient.getAccessToken(code);

        final GithubProfileResponse githubProfile = oAuthClient.getProfile(githubAccessToken);
        final Long memberId = getMemberIdByGithubProfile(githubProfile);

        final String token = jwtTokenProvider.createToken(memberId.toString());

        return new LoginResponse(memberId, token, githubProfile.getProfileUrl());
    }

    private Long getMemberIdByGithubProfile(final GithubProfileResponse githubProfile) {
        final int githubId = Integer.parseInt(githubProfile.getGithubId());

        return memberService.findByGithubId(githubId)
                .map(Member::getId)
                .orElseGet(() -> registerMember(githubProfile, githubId));
    }

    private Long registerMember(final GithubProfileResponse githubProfile, final int githubId) {
        return memberService.save(
                new MemberCreateDto(githubProfile.getNickname(), githubId, githubProfile.getProfileUrl()));
    }
}
