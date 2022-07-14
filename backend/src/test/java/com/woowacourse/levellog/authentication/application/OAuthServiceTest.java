package com.woowacourse.levellog.authentication.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.woowacourse.levellog.application.MemberService;
import com.woowacourse.levellog.authentication.JwtTokenProvider;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.dto.MemberCreateDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayName("OAuthService의")
class OAuthServiceTest {

    @MockBean
    private GithubOAuthClient githubOAuthClient;

    @MockBean
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private OAuthService oAuthService;

    @Nested
    @DisplayName("login 메서드는")
    class login {

        @Test
        @DisplayName("첫 로그인 시 회원가입하고 토큰과 이미지 URL를 반환한다.")
        void loginFirst() {
            // given
            when(githubOAuthClient.getGithubAccessToken("githubCode")).thenReturn("accessToken");
            when(githubOAuthClient.getGithubProfile("accessToken")).thenReturn(
                    new GithubProfileResponse("12345", "로마", "imageUrl"));
            when(memberService.findByGithubId(12345)).thenReturn(Optional.empty());

            // when
            final LoginResponse tokenResponse = oAuthService.login(new GithubCodeRequest("githubCode"));
            final int payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

            // then
            verify(memberService, times(1)).save(any(MemberCreateDto.class));
            assertThat(payload).isEqualTo(12345);
            assertThat(tokenResponse.getProfileUrl()).isEqualTo("imageUrl");
        }

        @Test
        @DisplayName("첫 로그인이 아닌 경우 회원가입 하지 않고 토큰과 이미지 URL를 반환한다.")
        void loginNotFirst() {
            // given
            when(githubOAuthClient.getGithubAccessToken("githubCode")).thenReturn("accessToken");
            when(githubOAuthClient.getGithubProfile("accessToken")).thenReturn(
                    new GithubProfileResponse("12345", "로마", "imageUrl"));

            final Member savedMember = new Member("로마", 12345, "imageUrl");
            when(memberService.findByGithubId(12345)).thenReturn(Optional.of(savedMember));

            // when
            final LoginResponse tokenResponse = oAuthService.login(new GithubCodeRequest("githubCode"));
            final int payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

            // then
            verify(memberService, never()).save(any(MemberCreateDto.class));
            assertThat(payload).isEqualTo(12345);
            assertThat(tokenResponse.getProfileUrl()).isEqualTo("imageUrl");
        }
    }

}
