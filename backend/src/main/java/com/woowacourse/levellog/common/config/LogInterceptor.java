package com.woowacourse.levellog.common.config;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final String correlationId = UUID.randomUUID()
                .toString()
                .substring(0, 8);
        MDC.put("correlationId", correlationId);
        return true;
    }
}
