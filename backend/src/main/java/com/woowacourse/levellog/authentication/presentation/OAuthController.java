package com.woowacourse.levellog.authentication.presentation;

import com.woowacourse.levellog.authentication.application.LoginService;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.dto.request.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.response.GithubProfileResponse;
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

    private final OAuthService oAuthService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final GithubCodeRequest request) {
        final GithubProfileResponse githubProfile = oAuthService.requestGithubProfile(request);
        return ResponseEntity.ok(loginService.login(githubProfile));
    }
}
