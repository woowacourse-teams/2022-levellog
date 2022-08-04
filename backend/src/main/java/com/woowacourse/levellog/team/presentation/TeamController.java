package com.woowacourse.levellog.team.presentation;

<<<<<<< HEAD
import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.TeamAndRoleDto;
import com.woowacourse.levellog.team.dto.TeamAndRolesDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
=======
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.authentication.support.NoAuthentication;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.dto.TeamRequest;
import com.woowacourse.levellog.team.dto.TeamResponse;
import com.woowacourse.levellog.team.dto.TeamUpdateRequest;
import com.woowacourse.levellog.team.dto.TeamsResponse;
>>>>>>> main
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
<<<<<<< HEAD
    public ResponseEntity<Void> save(@RequestBody @Valid final TeamCreateDto teamCreateDto,
                                     @Authentic final Long memberId) {
        final Long teamId = teamService.save(teamCreateDto, memberId);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId)).build();
    }

    @GetMapping
    @PublicAPI
    public ResponseEntity<TeamAndRolesDto> findAll(@Authentic final Long memberId) {
        final TeamAndRolesDto response = teamService.findAll(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    @PublicAPI
    public ResponseEntity<TeamAndRoleDto> findById(@PathVariable final Long teamId,
                                                   @Authentic final Long memberId) {
        final TeamAndRoleDto response = teamService.findByTeamIdAndMemberId(teamId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}/members/{targetMemberId}/my-role")
    public ResponseEntity<InterviewRoleDto> findMyRole(@PathVariable final Long teamId,
                                                       @PathVariable final Long targetMemberId,
                                                       @Authentic final Long memberId) {
        final InterviewRoleDto response = teamService.findMyRole(teamId, targetMemberId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @RequestBody @Valid final TeamUpdateDto request,
                                       @Authentic final Long memberId) {
        teamService.update(request, teamId, memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teamId}/close")
    public ResponseEntity<Void> close(@PathVariable final Long teamId,
                                      @Authentic final Long memberId) {
        teamService.close(teamId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> delete(@PathVariable final Long teamId, @Authentic final Long memberId) {
        teamService.deleteById(teamId, memberId);
=======
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable final Long id,
                                       @RequestBody @Valid final TeamUpdateRequest request,
                                       @LoginMember final Long memberId) {
        teamService.update(id, request, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id, @LoginMember final Long memberId) {
        teamService.deleteById(id, memberId);
>>>>>>> main
        return ResponseEntity.noContent().build();
    }
}
