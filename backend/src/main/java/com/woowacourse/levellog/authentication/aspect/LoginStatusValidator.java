package com.woowacourse.levellog.authentication.aspect;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.MemberRepository;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Service에 정의된 public 메서드 중 {@link com.woowacourse.levellog.authentication.support.Verified}가 달린
 * {@link com.woowacourse.levellog.common.dto.LoginStatus}를 찾는다. 해당하는 매개 변수가 있다면 LoginStatus에서 MemberId를 꺼내서 DB에 존재하는지
 * 유효성 검사를 수행한다.
 * <p>
 * ※※※LoginStatus 타입이 아닌 값에 @Verified를 붙이면 IllegalArgumentException이 터진다.※※※
 */
@Component
@Aspect
@RequiredArgsConstructor
public class LoginStatusValidator {

    private final MemberRepository memberRepository;

    @Before("execution(* com.woowacourse.levellog..*.application..*(..))")
    public void validateMemberId(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(this::isVerified)
                .map(this::castToLoginStatus)
                .filter(LoginStatus::isLogin)
                .map(LoginStatus::getMemberId)
                .findFirst()
                .ifPresent(memberRepository::getMember);
    }

    private boolean isVerified(final Object parameter) {
        return parameter.getClass().isAnnotationPresent(Verified.class);
    }

    private LoginStatus castToLoginStatus(final Object parameter) {
        if (!(parameter instanceof LoginStatus)) {
            throw new IllegalArgumentException("@Verified 어노테이션은 LoginStatus 객체에만 붙여야함");
        }
        return (LoginStatus) parameter;
    }
}
