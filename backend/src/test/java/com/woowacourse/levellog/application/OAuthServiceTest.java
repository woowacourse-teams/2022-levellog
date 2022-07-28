package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.authentication.dto.LoginDto;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OAuthService의")
class OAuthServiceTest extends ServiceTest {

    @Nested
    @DisplayName("login 메서드는")
    class login {

        @Test
        @DisplayName("첫 로그인 시 회원가입하고 id, 토큰, 이미지 URL를 반환한다.")
        void login_first_signUp() throws Exception {
            // given
            final GithubProfileDto request = new GithubProfileDto("1234", "릭", "rick.org");

            // when
            final LoginDto tokenResponse = oAuthService.login(
                    new GithubCodeDto(objectMapper.writeValueAsString(request)));

            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());
            final Long savedMemberId = memberService.findMemberById(Long.parseLong(payload))
                    .getId();

            // then
            assertAll(
                    () -> assertThat(Long.parseLong(payload)).isEqualTo(savedMemberId),
                    () -> assertThat(tokenResponse.getProfileUrl()).isEqualTo("rick.org"),
                    () -> assertThat(tokenResponse.getId()).isNotNull()
            );
        }

        @Test
        @DisplayName("첫 로그인이 아닌 경우 회원가입 하지 않고 토큰과 이미지 URL를 반환한다.")
        void login_notFirst_signIn() throws Exception {
            // given
            final Long savedId = memberService.save(new MemberCreateDto("로마", 12345, "imageUrl"));

            final GithubProfileDto request = new GithubProfileDto("12345", "로마", "imageUrl");

            // when
            final LoginDto tokenResponse = oAuthService.login(
                    new GithubCodeDto(objectMapper.writeValueAsString(request)));
            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());

            // then
            assertAll(
                    () -> assertThat(Long.parseLong(payload)).isEqualTo(savedId),
                    () -> assertThat(tokenResponse.getProfileUrl()).isEqualTo("imageUrl"),
                    () -> assertThat(tokenResponse.getId()).isNotNull()
            );
        }
    }
}
