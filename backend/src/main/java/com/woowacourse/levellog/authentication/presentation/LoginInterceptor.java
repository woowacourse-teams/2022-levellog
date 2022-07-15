package com.woowacourse.levellog.authentication.presentation;

import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;

import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.support.AuthorizationExtractor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (isPreFlightRequest(request)) {
            return true;
        }

        final String token = AuthorizationExtractor.extract(request);

        jwtTokenProvider.getPayload(token);
        return true;
    }
}
