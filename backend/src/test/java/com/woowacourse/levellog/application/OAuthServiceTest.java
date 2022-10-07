package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.authentication.dto.LoginDto;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OAuthService의")
class OAuthServiceTest extends ServiceTest {

    @Nested
    @DisplayName("login 메서드는")
    class Login {

        @Test
        @DisplayName("첫 로그인 시 회원가입하고 id, 토큰, 이미지 URL를 반환한다.")
        void login_ifFirst_success() throws Exception {
            // given
            final GithubProfileDto request = new GithubProfileDto("1234", "릭", "rick.org");

            // when
            final LoginDto tokenResponse = oAuthService.login(
                    new GithubCodeDto(objectMapper.writeValueAsString(request)));

            // then
            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());
            final Long savedMemberId = memberService.findMemberById(LoginStatus.fromLogin(Long.parseLong(payload)))
                    .getId();
            assertAll(
                    () -> assertThat(Long.parseLong(payload)).isEqualTo(savedMemberId),
                    () -> assertThat(tokenResponse.getProfileUrl()).isEqualTo("rick.org"),
                    () -> assertThat(tokenResponse.getNickname()).isEqualTo("릭"),
                    () -> assertThat(tokenResponse.getId()).isNotNull()
            );
        }

        @Test
        @DisplayName("첫 로그인이 아닌 경우 회원가입 하지 않고 토큰과 이미지 URL를 반환한다.")
        void login_ifNotFirst_success() throws Exception {
            // given
            final Member member = saveMember("로마");

            final GithubProfileDto request = new GithubProfileDto(member.getGithubId().toString(), member.getNickname(),
                    member.getProfileUrl());

            // when
            final LoginDto tokenResponse = oAuthService.login(
                    new GithubCodeDto(objectMapper.writeValueAsString(request)));

            // then
            final String payload = jwtTokenProvider.getPayload(tokenResponse.getAccessToken());
            assertAll(
                    () -> assertThat(Long.parseLong(payload)).isEqualTo(member.getId()),
                    () -> assertThat(tokenResponse.getId()).isNotNull()
            );
        }
    }
}
