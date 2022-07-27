package com.woowacourse.levellog.team.presentation;

import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.authentication.support.NoAuthentication;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final TeamCreateDto teamCreateDto,
                                     @LoginMember final Long memberId) {
        final Long teamId = teamService.save(memberId, teamCreateDto);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId)).build();
    }

    @GetMapping
    @NoAuthentication
    public ResponseEntity<TeamsDto> findAll() {
        final TeamsDto response = teamService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    @NoAuthentication
    public ResponseEntity<TeamDto> findById(@PathVariable final Long teamId) {
        final TeamDto response = teamService.findById(teamId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @RequestBody @Valid final TeamUpdateDto request,
                                       @LoginMember final Long memberId) {
        teamService.update(teamId, request, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable final Long teamId, @LoginMember final Long memberId) {
        teamService.deleteById(teamId, memberId);
        return ResponseEntity.noContent().build();
    }
}
