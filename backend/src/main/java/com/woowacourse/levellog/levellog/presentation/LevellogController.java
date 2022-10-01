package com.woowacourse.levellog.levellog.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams/{teamId}/levellogs")
@RequiredArgsConstructor
public class LevellogController {

    private final LevellogService levellogService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long teamId,
                                     @RequestBody @Valid final LevellogWriteDto request,
                                     @Authentic LoginStatus loginStatus) {
        final Long id = levellogService.save(request, loginStatus, teamId);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId + "/levellogs/" + id)).build();
    }

    @GetMapping("/{levellogId}")
    @PublicAPI
    public ResponseEntity<LevellogDto> find(@PathVariable final Long teamId,
                                            @PathVariable final Long levellogId) {
        final LevellogDto response = levellogService.findById(levellogId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{levellogId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @PathVariable final Long levellogId,
                                       @Authentic LoginStatus loginStatus,
                                       @RequestBody @Valid final LevellogWriteDto request) {
        levellogService.update(request, levellogId, loginStatus);
        return ResponseEntity.noContent().build();
    }
}
