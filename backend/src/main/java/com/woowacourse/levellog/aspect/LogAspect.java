package com.woowacourse.levellog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(* com.woowacourse.levellog..*(..))")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.currentTimeMillis();
        final Object result;
        result = joinPoint.proceed();
        final long endTime = System.currentTimeMillis();

        final String correlationId = MDC.get("correlationId");
        final long executionTimeMillis = endTime - startTime;
        final String declaringTypeName = joinPoint.getSignature()
                .getDeclaringTypeName();
        log.info("[{}]-{} took {}ms", correlationId, declaringTypeName, executionTimeMillis);
        return result;
    }
}
