package com.woowacourse.levellog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.support.TimeStandard;
import org.mindrot.jbcrypt.BCrypt;
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

    @Bean
    public AdminService adminService(final JwtTokenProvider jwtTokenProvider, final TimeStandard timeStandard,
                                     final TeamRepository teamRepository) {
        final String salt = BCrypt.gensalt();
        final String hash = BCrypt.hashpw("levellog1!", salt);

        return new AdminService(hash, jwtTokenProvider, timeStandard, teamRepository);
    }
}
