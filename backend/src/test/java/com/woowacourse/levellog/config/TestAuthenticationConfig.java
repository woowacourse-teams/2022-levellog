package com.woowacourse.levellog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestAuthenticationConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Primary
    public OAuthClient fakeOAuthClient() {
        return new FakeGithubOAuthClient(objectMapper);
    }
}
