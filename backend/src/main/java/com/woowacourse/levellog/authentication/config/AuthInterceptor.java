package com.woowacourse.levellog.authentication.config;

import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;

import com.woowacourse.levellog.authentication.exception.InvalidTokenException;
import com.woowacourse.levellog.authentication.support.AuthorizationExtractor;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.common.support.DebugMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (requireTokenValidation(request, handler)) {
            validateToken(request);
        }
        return true;
    }

    private boolean requireTokenValidation(
            final HttpServletRequest request,
            final Object handler
    ) {
        if (isPreFlightRequest(request)) {
            return false;
        }
        return !isPublicAPI((HandlerMethod) handler);
    }

    private boolean isPublicAPI(final HandlerMethod handler) {
        final PublicAPI classTypePublicAPI = handler.getBeanType().getAnnotation(PublicAPI.class);
        final boolean isPublicClass = classTypePublicAPI != null;

        final PublicAPI methodPublicAPI = handler.getMethodAnnotation(PublicAPI.class);
        final boolean isPublicMethod = methodPublicAPI != null;

        return isPublicClass || isPublicMethod;
    }

    private void validateToken(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException(DebugMessage.init().append("token", token));
        }
    }
}
