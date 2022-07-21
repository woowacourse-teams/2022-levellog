package com.woowacourse.levellog.authentication.presentation;

import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;

import com.woowacourse.levellog.authentication.config.NoAuthentication;
import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.exception.InvalidTokenException;
import com.woowacourse.levellog.authentication.support.AuthorizationExtractor;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (isPreFlightRequest(request)) {
            return true;
        }

        if (isAnnotatedNoAuthentication((HandlerMethod) handler)) {
            return true;
        }

        final String token = AuthorizationExtractor.extract(request);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }

        return true;
    }

    private boolean isAnnotatedNoAuthentication(HandlerMethod handler) {
        NoAuthentication classTypeNoAuthentication = handler.getBeanType().getAnnotation(NoAuthentication.class);
        NoAuthentication methodNoAuthentication = handler.getMethodAnnotation(NoAuthentication.class);

        return !Objects.isNull(classTypeNoAuthentication) || !Objects.isNull(methodNoAuthentication);
    }
}
