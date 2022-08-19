package com.woowacourse.levellog.admin.presentation;

import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.admin.dto.AdminAccessTokenDto;
import com.woowacourse.levellog.admin.dto.AdminTeamDto;
import com.woowacourse.levellog.admin.dto.PasswordDto;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
@PublicAPI
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String login() {
        return "admin-login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<AdminAccessTokenDto> login(@RequestBody @Valid final PasswordDto request) {
        final AdminAccessTokenDto response = adminService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teams")
    public String findAllTeams(final Model model) {
        final List<AdminTeamDto> response = adminService.findAll();
        model.addAttribute("teams", response);
        return "admin-teams";
    }
}
