package com.woowacourse.levellog.authentication.config;

import com.woowacourse.levellog.authentication.support.AuthorizationExtractor;
import com.woowacourse.levellog.authentication.support.Extracted;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.common.dto.LoginStatus;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginStatusResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Extracted.class);
    }

    @Override
    public LoginStatus resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                       final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String token = AuthorizationExtractor.extract(request);
        if (token == null) {
            return LoginStatus.fromNotLogin();
        }

        final String payload = jwtTokenProvider.getPayload(token);
        final long memberId = Long.parseLong(payload);
        return LoginStatus.fromLogin(memberId);
    }
}
