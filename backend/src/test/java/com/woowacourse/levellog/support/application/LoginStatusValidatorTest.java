package com.woowacourse.levellog.support.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.authentication.aspect.LoginStatusValidator;
import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

@DisplayName("LoginStatusValidator의")
@SpringBootTest
class LoginStatusValidatorTest {

    @Autowired
    private LoginStatusValidator aspect;

    @Autowired
    private MemberRepository memberRepository;

    private TestService proxyService;

    @BeforeEach
    void setUp() {
        final AspectJProxyFactory aspectJProxyFactory = new AspectJProxyFactory(new TestService());
        aspectJProxyFactory.addAspect(aspect);

        final DefaultAopProxyFactory proxyFactory = new DefaultAopProxyFactory();
        final AopProxy aopProxy = proxyFactory.createAopProxy(aspectJProxyFactory);

        proxyService = (TestService) aopProxy.getProxy();
    }

    @Service
    public static class TestService {

        Long someMethod(@Verified final LoginStatus loginStatus) {
            return loginStatus.getMemberId();
        }
    }

    @Nested
    @DisplayName("validateMemberId 메서드는")
    class ValidateMemberId {

        @Test
        @DisplayName("토큰에서 추출한 memberId에 해당하는 회원이 존재여부를 확인한다.")
        void success() {
            // given
            final Member member = new Member("릭 (우형 합격 예정)", 1234, "levellog.app");
            final Long memberId = memberRepository.save(member)
                    .getId();
            final LoginStatus loginStatus = LoginStatus.fromLogin(memberId);

            // when
            final Long actual = proxyService.someMethod(loginStatus);

            // then
            assertThat(actual).isEqualTo(memberId);
        }

        @Test
        @DisplayName("토큰에서 추출한 memberId에 해당하는 회원이 존재하지 않으면 예외를 던진다.")
        void validateMemberId_notExistMember_exception() {
            // given
            final LoginStatus loginStatus = LoginStatus.fromLogin(777L);

            // when & then
            assertThatThrownBy(() -> proxyService.someMethod(loginStatus))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}
