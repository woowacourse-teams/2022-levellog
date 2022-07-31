package com.woowacourse.levellog.interview_question.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.interview_question.application.InterviewQuestionService;
import com.woowacourse.levellog.interview_question.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interview_question.dto.InterviewQuestionsDto;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/levellogs/{levellogId}/interview-questions")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private final InterviewQuestionService interviewQuestionService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long levellogId,
                                     @RequestBody @Valid final InterviewQuestionDto request,
                                     @Authentic final Long memberId) {
        final Long interviewQuestionId = interviewQuestionService.save(request, levellogId, memberId);
        return ResponseEntity.created(
                URI.create("/api/levellogs/" + levellogId + "/interview-questions/" + interviewQuestionId)).build();
    }

    @GetMapping
    public ResponseEntity<InterviewQuestionsDto> findAll(@PathVariable final Long levellogId,
                                                         @Authentic final Long memberId) {
        final InterviewQuestionsDto response = interviewQuestionService.findAll(levellogId, memberId);
        return ResponseEntity.ok(response);
    }
}
