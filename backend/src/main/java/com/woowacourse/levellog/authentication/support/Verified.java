package com.woowacourse.levellog.authentication.support;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 어노테이션을 Service의 메서드에 매개 변수 중 {@link com.woowacourse.levellog.common.dto.LoginStatus}에 달면
 * {@link com.woowacourse.levellog.authentication.aspect.LoginStatusValidator}가 LoginStatus의 MemberId를 꺼내서
 * MemberRepository를 통해 조회하는 유효성 검사를 수행한다.
 * <p>
 * ※※※해당 어노테이션은 Service에서 사용되어야 한다.※※※
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Verified {
}
