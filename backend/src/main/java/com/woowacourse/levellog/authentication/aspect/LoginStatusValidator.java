package com.woowacourse.levellog.authentication.aspect;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.common.dto.LoginStatus;
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
public class LoginStatusValidator {

    private final MemberRepository memberRepository;

    @Before("execution(* com.woowacourse.levellog..*.application..*(..))")
    public void validateMemberId(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(this::isAuthentic)
                .map(this::castToLoginStatus)
                .filter(LoginStatus::isLogin)
                .map(LoginStatus::getMemberId)
                .findFirst()
                .ifPresent(memberRepository::getMember);
    }

    private boolean isAuthentic(final Object parameter) {
        return parameter.getClass().isAnnotationPresent(Authentic.class);
    }

    private LoginStatus castToLoginStatus(final Object parameter) {
        if (!(parameter instanceof LoginStatus)) {
            throw new IllegalArgumentException("@Authentic 어노테이션은 LoginStatus 객체에만 붙여야함");
        }
        return (LoginStatus) parameter;
    }
}
