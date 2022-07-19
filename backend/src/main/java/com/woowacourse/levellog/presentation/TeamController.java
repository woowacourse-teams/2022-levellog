package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.TeamService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.dto.TeamRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
