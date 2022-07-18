package com.woowacourse.levellog.authentication.support;

import com.woowacourse.levellog.authentication.domain.OAuthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestAuthenticationConfig {

    @Bean
    @Primary
    public OAuthClient fakeOAuthClient() {
        return new FakeGithubOAuthClient();
    }
}
