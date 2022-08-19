package com.woowacourse.levellog.admin.presentation;

import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.PasswordDto;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    @PublicAPI
    @ResponseBody
    public ResponseEntity<AdminAccessTokenDto> login(@RequestBody @Valid final PasswordDto request) {
        final AdminAccessTokenDto response = adminService.login(request);
        return ResponseEntity.ok(response);
    }
}
