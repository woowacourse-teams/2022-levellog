package com.woowacourse.levellog.admin.presentation;

import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.admin.dto.request.AdminPasswordRequest;
import com.woowacourse.levellog.admin.dto.response.AdminAccessTokenResponse;
import com.woowacourse.levellog.admin.dto.response.AdminTeamResponse;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<AdminAccessTokenResponse> login(@RequestBody @Valid final AdminPasswordRequest request) {
        final AdminAccessTokenResponse response = adminService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teams")
    public String findAllTeam(final Model model) {
        final List<AdminTeamResponse> response = adminService.findAllTeam();
        model.addAttribute("teams", response);
        return "admin-teams";
    }

    @DeleteMapping("/teams/{teamId}")
    @ResponseBody
    public ResponseEntity<Void> deleteTeam(@PathVariable final Long teamId) {
        adminService.deleteTeamById(teamId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/teams/{teamId}/close")
    @ResponseBody
    public ResponseEntity<Void> closeTeam(@PathVariable final Long teamId) {
        adminService.closeTeam(teamId);
        return ResponseEntity.noContent().build();
    }
}
