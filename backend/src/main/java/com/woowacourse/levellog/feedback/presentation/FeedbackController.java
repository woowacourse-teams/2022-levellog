package com.woowacourse.levellog.feedback.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.FeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
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
@RequestMapping("/api/levellogs/{levellogId}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long levellogId,
                                     @RequestBody @Valid final FeedbackWriteDto request,
                                     @Authentic final Long memberId) {
        final Long id = feedbackService.save(request, levellogId, memberId);
        return ResponseEntity.created(URI.create("/api/levellogs/" + levellogId + "/feedbacks/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<FeedbacksDto> findAll(@PathVariable final Long levellogId,
                                                @Authentic final Long memberId) {
        final FeedbacksDto response = feedbackService.findAll(levellogId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{feedbackId}")
    public ResponseEntity<FeedbackDto> findById(@PathVariable final Long levellogId,
                                               @PathVariable final Long feedbackId,
                                               @Authentic final Long memberId) {
        final FeedbackDto response = feedbackService.findById(levellogId, feedbackId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<Void> update(@PathVariable final Long levellogId,
                                       @RequestBody @Valid final FeedbackWriteDto request,
                                       @PathVariable final Long feedbackId,
                                       @Authentic final Long memberId) {
        feedbackService.update(request, feedbackId, memberId);
        return ResponseEntity.noContent().build();
    }
}
