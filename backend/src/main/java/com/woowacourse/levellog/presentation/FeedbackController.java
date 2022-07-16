package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.FeedbackService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levellogs/{levellogId}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestParam final Long levellogId,
                                     @RequestBody @Valid final FeedbackCreateRequest request,
                                     @LoginMember final Member member) {
        feedbackService.save(levellogId, member, request);
        return ResponseEntity.created(URI.create("/api/feedbacks")).build();
    }

    @GetMapping
    public ResponseEntity<FeedbacksResponse> findAll(@RequestParam final Long levellogId) {
        final FeedbacksResponse response = feedbackService.findAll(levellogId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Void> delete(@RequestParam final Long levellogId,
                                       @PathVariable final Long feedbackId) {
        feedbackService.deleteById(feedbackId);
        return ResponseEntity.noContent().build();
    }
}
