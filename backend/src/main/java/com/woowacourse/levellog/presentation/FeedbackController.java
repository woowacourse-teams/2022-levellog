package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.FeedbackService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid final FeedbackCreateRequest request) {
        feedbackService.save(request);
        return ResponseEntity.created(URI.create("/api/feedbacks")).build();
    }

    @GetMapping
    public ResponseEntity<FeedbacksResponse> findAll() {
        final FeedbacksResponse response = feedbackService.findAll();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        feedbackService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
