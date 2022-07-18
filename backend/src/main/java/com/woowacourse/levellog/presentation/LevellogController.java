package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.LevellogService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.domain.Member;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams/{teamId}/levellogs")
@RequiredArgsConstructor
public class LevellogController {

    private final LevellogService levellogService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam final Long teamId,
                                       @RequestBody @Valid final LevellogRequest request,
                                       @LoginMember final Member author) {
        final Long id = levellogService.save(author, teamId, request);
        return ResponseEntity.created(URI.create("/api/teams/" + teamId + "/levellogs/" + id)).build();
    }

    @GetMapping("/{levellogId}")
    public ResponseEntity<LevellogResponse> find(@RequestParam final Long teamId,
                                                 @PathVariable final Long levellogId) {
        final LevellogResponse response = levellogService.findById(levellogId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{levellogId}")
    public ResponseEntity<Void> update(@RequestParam final Long teamId,
                                       @PathVariable final Long levellogId,
                                       @RequestBody @Valid final LevellogRequest request) {
        levellogService.update(levellogId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{levellogId}")
    public ResponseEntity<Void> delete(@RequestParam final Long teamId,
                                       @PathVariable final Long levellogId) {
        levellogService.deleteById(levellogId);
        return ResponseEntity.noContent().build();
    }
}
