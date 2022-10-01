package com.woowacourse.levellog.team.presentation;

import com.woowacourse.levellog.authentication.support.FromToken;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamListDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TeamQueryService teamQueryService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final TeamWriteDto teamDto,
                                     @FromToken final LoginStatus loginStatus) {
        final Long teamId = teamService.save(teamDto, loginStatus);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId)).build();
    }

    @GetMapping
    @PublicAPI
    public ResponseEntity<TeamListDto> findAll(@RequestParam(defaultValue = "open") final String condition,
                                               @RequestParam(defaultValue = "0") final int page,
                                               @RequestParam(defaultValue = "20") final int size) {
        final TeamFilterCondition filterCondition = TeamFilterCondition.from(condition);
        final TeamListDto response = teamQueryService.findAll(filterCondition, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    @PublicAPI
    public ResponseEntity<TeamDto> findById(@PathVariable final Long teamId,
                                            @FromToken final LoginStatus loginStatus) {
        final TeamDto response = teamQueryService.findByTeamIdAndMemberId(teamId, loginStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}/status")
    @PublicAPI
    public ResponseEntity<TeamStatusDto> findStatus(@PathVariable final Long teamId) {
        final TeamStatusDto response = teamService.findStatus(teamId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}/members/{targetMemberId}/my-role")
    public ResponseEntity<InterviewRoleDto> findMyRole(@PathVariable final Long teamId,
                                                       @PathVariable final Long targetMemberId,
                                                       @FromToken final LoginStatus loginStatus) {
        final InterviewRoleDto response = teamService.findMyRole(teamId, targetMemberId, loginStatus);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @RequestBody @Valid final TeamWriteDto request,
                                       @FromToken final LoginStatus loginStatus) {
        teamService.update(request, teamId, loginStatus);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/close")
    public ResponseEntity<Void> close(@PathVariable final Long teamId,
                                      @FromToken final LoginStatus loginStatus) {
        teamService.close(teamId, loginStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable final Long teamId,
                                       @FromToken final LoginStatus loginStatus) {
        teamService.deleteById(teamId, loginStatus);
        return ResponseEntity.noContent().build();
    }
}
