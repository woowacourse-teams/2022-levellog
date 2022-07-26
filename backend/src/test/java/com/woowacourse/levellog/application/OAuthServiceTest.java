package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
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
    private OAuthClient oAuthClient;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private OAuthService oAuthService;

    @Nested
    @DisplayName("login 메서드는")
    class login {

        @Test
        @DisplayName("첫 로그인 시 회원가입하고 id, 토큰, 이미지 URL를 반환한다.")
        void loginFirst() {
            // given
            given(oAuthClient.getAccessToken("githubCode")).willReturn("accessToken");
            given(oAuthClient.getProfile("accessToken")).willReturn(
                    new GithubProfileResponse("12345", "로마", "imageUrl"));

            // when
            final LoginResponse tokenResponse = oAuthService.login(new GithubCodeRequest("githubCode"));
            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());
            final Optional<Member> savedMember = memberService.findByGithubId(12345);

            // then
            assertThat(savedMember).isPresent();
            assertThat(Long.parseLong(payload)).isEqualTo(savedMember.get().getId());
            assertThat(tokenResponse.getProfileUrl()).isEqualTo("imageUrl");
            assertThat(tokenResponse.getId()).isNotNull();
        }

        @Test
        @DisplayName("첫 로그인이 아닌 경우 회원가입 하지 않고 토큰과 이미지 URL를 반환한다.")
        void loginNotFirst() {
            // given
            final Long savedId = memberService.save(new MemberCreateDto("로마", 12345, "imageUrl"));

            given(oAuthClient.getAccessToken("githubCode")).willReturn("accessToken");
            given(oAuthClient.getProfile("accessToken")).willReturn(
                    new GithubProfileResponse("12345", "로마", "imageUrl"));

            // when
            final LoginResponse tokenResponse = oAuthService.login(new GithubCodeRequest("githubCode"));
            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

            // then
            assertThat(Long.parseLong(payload)).isEqualTo(savedId);
            assertThat(tokenResponse.getProfileUrl()).isEqualTo("imageUrl");
            assertThat(tokenResponse.getId()).isNotNull();
        }
    }
}
