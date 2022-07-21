package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.TeamService;
import com.woowacourse.levellog.authentication.config.NoAuthentication;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.dto.TeamRequest;
import com.woowacourse.levellog.dto.TeamResponse;
import com.woowacourse.levellog.dto.TeamsResponse;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final TeamRequest teamRequest,
                                     @LoginMember final Long hostId) {
        final Long id = teamService.save(hostId, teamRequest);
        return ResponseEntity.created(URI.create("/api/teams/" + id)).build();
    }

    @GetMapping
    @NoAuthentication
    public ResponseEntity<TeamsResponse> findAll() {
        final TeamsResponse response = teamService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @NoAuthentication
    public ResponseEntity<TeamResponse> findById(@PathVariable final Long id) {
        final TeamResponse response = teamService.findById(id);
        return ResponseEntity.ok(response);
    }
}
