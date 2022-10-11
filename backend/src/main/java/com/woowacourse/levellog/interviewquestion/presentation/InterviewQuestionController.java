package com.woowacourse.levellog.interviewquestion.presentation;

import com.woowacourse.levellog.authentication.support.Extracted;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponses;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionResponses;
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
@RequestMapping("/api/levellogs/{levellogId}/interview-questions")
@RequiredArgsConstructor
public class InterviewQuestionController {

    private final InterviewQuestionService interviewQuestionService;

    @PostMapping
    public ResponseEntity<Void> save(@PathVariable final Long levellogId,
                                     @RequestBody @Valid final InterviewQuestionWriteRequest request,
                                     @Extracted final LoginStatus loginStatus) {
        final Long interviewQuestionId = interviewQuestionService.save(request, levellogId, loginStatus);
        return ResponseEntity.created(
                URI.create("/api/levellogs/" + levellogId + "/interview-questions/" + interviewQuestionId)).build();
    }

    @GetMapping
    @PublicAPI
    public ResponseEntity<InterviewQuestionResponses> findAllByLevellog(@PathVariable final Long levellogId) {
        final InterviewQuestionResponses response = interviewQuestionService.findAllByLevellog(levellogId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<InterviewQuestionContentResponses> findAllMyInterviewQuestion(
            @PathVariable final Long levellogId,
            @Extracted final LoginStatus loginStatus) {
        final InterviewQuestionContentResponses response = interviewQuestionService.findAllByLevellogAndAuthor(
                levellogId,
                loginStatus);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{interviewQuestionId}")
    public ResponseEntity<Void> update(@PathVariable final Long levellogId,
                                       @PathVariable final Long interviewQuestionId,
                                       @RequestBody @Valid final InterviewQuestionWriteRequest request,
                                       @Extracted final LoginStatus loginStatus) {
        interviewQuestionService.update(request, interviewQuestionId, loginStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{interviewQuestionId}")
    public ResponseEntity<Void> deleteById(@PathVariable final Long levellogId,
                                           @PathVariable final Long interviewQuestionId,
                                           @Extracted final LoginStatus loginStatus) {
        interviewQuestionService.deleteById(interviewQuestionId, loginStatus);
        return ResponseEntity.noContent().build();
    }
}
