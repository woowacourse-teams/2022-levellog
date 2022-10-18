package com.woowacourse.levellog.authentication.presentation;

import com.woowacourse.levellog.authentication.application.MemberLogin;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.LoginResponse;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@PublicAPI
public class OAuthController {

    private final MemberLogin memberLogin;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final GithubCodeRequest request) {
        return ResponseEntity.ok(memberLogin.login(request));
    }
}
