package com.woowacourse.levellog.authentication.presentation;

import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.config.NoAuthentication;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@NoAuthentication
public class OAuthController {

    private final OAuthService oAuthService;

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final GithubCodeRequest request) {
        return ResponseEntity.ok(oAuthService.login(request));
    }
}
