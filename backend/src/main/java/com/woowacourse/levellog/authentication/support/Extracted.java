package com.woowacourse.levellog.authentication.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 해당 어노테이션을 Controller의 메서드에 매개 변수 중 {@link com.woowacourse.levellog.common.dto.LoginStatus}에 달면
 * {@link com.woowacourse.levellog.authentication.config.LoginStatusResolver}가 토큰에서 MemberId를 추출해서 LoginStatus를 생성 후 매개
 * 변수에 세팅한다.
 * <p>
 * ※※※해당 어노테이션은 Controller에서 사용되어야 한다.※※※
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Extracted {
}
