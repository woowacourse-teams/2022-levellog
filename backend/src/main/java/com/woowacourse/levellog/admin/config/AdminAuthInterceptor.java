package com.woowacourse.levellog.admin.config;

import com.woowacourse.levellog.authentication.exception.InvalidTokenException;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final String token = request.getParameter("token");
        if (!jwtTokenProvider.validateAdminToken(token)) {
            throw new InvalidTokenException("토큰 인증 실패 - token:" + token);
        }

        return true;
    }
}
