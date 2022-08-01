package com.woowacourse.levellog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.team.support.TimeStandard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    @Primary
    public OAuthClient fakeOAuthClient() {
        return new FakeGithubOAuthClient(objectMapper);
    }

    @Bean
    @Primary
    public TimeStandard fakeTimeStandard() {
        return new FakeTimeStandard();
    }
}
