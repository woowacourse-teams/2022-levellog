package com.woowacourse.levellog.authentication.aspect;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.member.domain.MemberRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class AuthenticMemberValidator {

    private static final int NOT_LOGIN_MEMBER_ID = -1;

    private final MemberRepository memberRepository;

    @Before("execution(* com.woowacourse.levellog..*.application..*(..))")
    public void checkAuthenticMemberId(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(this::isAuthentic)
                .mapToLong(Long.class::cast)
                .filter(this::isLoginMemberId)
                .findFirst()
                .ifPresent(memberRepository::getMember);
    }

    private boolean isAuthentic(final Object parameter) {
        return parameter.getClass().isAnnotationPresent(Authentic.class);
    }

    private boolean isLoginMemberId(final long memberId) {
        return memberId != NOT_LOGIN_MEMBER_ID;
    }
}
