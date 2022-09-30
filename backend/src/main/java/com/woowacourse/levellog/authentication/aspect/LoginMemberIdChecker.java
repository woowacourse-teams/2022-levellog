package com.woowacourse.levellog.authentication.aspect;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class LoginMemberIdChecker {

    private final MemberRepository memberRepository;

    @Before("execution(* com.woowacourse.levellog..*.application..*(..))")
    public void checkAuthenticMemberId(final JoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        for (final Object arg : args) {
            final Authentic authentic = arg.getClass().getDeclaredAnnotation(Authentic.class);
            if (authentic == null) {
                continue;
            }
            final Long memberId = (Long) arg;
            if (memberId == -1) {
                continue;
            }
            memberRepository.getMember(memberId);
        }
    }
}
