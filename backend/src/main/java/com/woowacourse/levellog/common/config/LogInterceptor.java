package com.woowacourse.levellog.common.config;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        final String correlationId = UUID.randomUUID()
                .toString()
                .substring(0, 8);
        MDC.put("correlationId", correlationId);

        final String httpMethod = request.getMethod();
        final String uri = request.getRequestURI();

        String queryString = request.getQueryString();
        if (queryString == null) {
            queryString = "";
        } else {
            queryString = "?" + queryString;
        }

        log.info("{} {}{}", httpMethod, uri, queryString);

        return true;
    }
}
