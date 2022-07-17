package com.woowacourse.levellog.authentication.config;

import com.woowacourse.levellog.authentication.domain.GithubOAuthClient;
import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.domain.OAuthClient;
import com.woowacourse.levellog.authentication.presentation.LoginInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig implements WebMvcConfigurer {

    private final LoginMemberResolver loginMemberResolver;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public OAuthClient oAuthClient(@Value("${security.github.client-id}") final String clientId,
                                   @Value("${security.github.client-secret}") final String clientSecret) {
        return new GithubOAuthClient(clientId, clientSecret);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtTokenProvider))
                .excludePathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberResolver);
    }
}
