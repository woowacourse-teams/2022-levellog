package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.LevellogService;
import com.woowacourse.levellog.authentication.config.NoAuthentication;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
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
@RequestMapping("/api/teams/{teamId}/levellogs")
@RequiredArgsConstructor
public class LevellogController {

    private final LevellogService levellogService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long teamId,
                                     @RequestBody @Valid final LevellogRequest request,
                                     @LoginMember final Long authorId) {
        final Long id = levellogService.save(authorId, teamId, request);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId + "/levellogs/" + id)).build();
    }

    @GetMapping("/{levellogId}")
    @NoAuthentication
    public ResponseEntity<LevellogResponse> find(@PathVariable final Long teamId,
                                                 @PathVariable final Long levellogId) {
        final LevellogResponse response = levellogService.findById(levellogId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{levellogId}")
    public ResponseEntity<Void> update(@PathVariable final Long teamId,
                                       @PathVariable final Long levellogId,
                                       @LoginMember final Long memberId,
                                       @RequestBody @Valid final LevellogRequest request
    ) {
        levellogService.update(levellogId, memberId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{levellogId}")
    public ResponseEntity<Void> delete(@PathVariable final Long teamId,
                                       @PathVariable final Long levellogId,
                                       @LoginMember final Long memberId) {
        levellogService.deleteById(levellogId, memberId);
        return ResponseEntity.noContent().build();
    }
}
