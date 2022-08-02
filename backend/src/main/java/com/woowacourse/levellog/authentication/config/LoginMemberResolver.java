package com.woowacourse.levellog.authentication.config;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.AuthorizationExtractor;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * PublicAPI 중 토크에서 member id를 꺼내야하는 기능이 추가되었다.
 * 이에 따라 토큰이 존재하지 않으면 -1을 반환하는 로직이 추가되었다.
 * @see com.woowacourse.levellog.team.presentation.TeamController#findById(Long, Long) 
 */
@Component
@RequiredArgsConstructor
public class LoginMemberResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authentic.class);
    }

    @Override
    public Long resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = AuthorizationExtractor.extract(request);
        if (token == null) {
            return -1L;
        }

        return Long.parseLong(jwtTokenProvider.getPayload(token));
    }
}
