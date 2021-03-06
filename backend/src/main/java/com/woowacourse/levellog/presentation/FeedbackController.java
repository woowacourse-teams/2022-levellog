package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.FeedbackService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.dto.FeedbackRequest;
import com.woowacourse.levellog.dto.FeedbacksResponse;
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
@RequestMapping("/api/levellogs/{levellogId}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long levellogId,
                                     @RequestBody @Valid final FeedbackRequest request,
                                     @LoginMember final Long memberId) {
        final Long id = feedbackService.save(levellogId, memberId, request);
        return ResponseEntity.created(URI.create("/api/levellogs/" + levellogId + "/feedbacks/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<FeedbacksResponse> findAll(@PathVariable final Long levellogId,
                                                     @LoginMember final Long memberId) {
        final FeedbacksResponse response = feedbackService.findAll(levellogId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Void> update(@PathVariable final Long levellogId,
                                       @RequestBody @Valid final FeedbackRequest request,
                                       @PathVariable final Long feedbackId) {
        feedbackService.update(feedbackId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> delete(@PathVariable final Long levellogId,
                                       @PathVariable final Long feedbackId,
                                       @LoginMember final Long memberId) {
        feedbackService.deleteById(feedbackId, memberId);
        return ResponseEntity.noContent().build();
    }
}
