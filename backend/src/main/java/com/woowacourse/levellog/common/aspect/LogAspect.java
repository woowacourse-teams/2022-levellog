package com.woowacourse.levellog.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

        final long executionTimeMillis = endTime - startTime;

        final String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();
        final String methodName = joinPoint.getSignature()
                .getName();

        log.info("{}.{} took {}ms", className, methodName, executionTimeMillis);

        return result;
    }
}
