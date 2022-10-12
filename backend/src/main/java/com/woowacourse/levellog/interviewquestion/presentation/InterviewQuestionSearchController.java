package com.woowacourse.levellog.interviewquestion.presentation;

import com.woowacourse.levellog.authentication.support.Extracted;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.StringConverter;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResults;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interview-questions")
@RequiredArgsConstructor
public class InterviewQuestionSearchController {

    private final InterviewQuestionService interviewQuestionService;

    @GetMapping
    @PublicAPI
    public ResponseEntity<InterviewQuestionSearchQueryResults> searchBy(
            @RequestParam(defaultValue = "") final String keyword,
            @RequestParam(defaultValue = "10") final Long size,
            @RequestParam(defaultValue = "0") final Long page,
            @RequestParam(defaultValue = "likes") final String sort,
            @Extracted final LoginStatus loginStatus) {
        final String input = StringConverter.toSafeString(keyword);
        if (input.isBlank()) {
            return ResponseEntity.ok(new InterviewQuestionSearchQueryResults(new ArrayList<>(), 0L));
        }
        final InterviewQuestionSearchQueryResults response = interviewQuestionService
                .searchByKeyword(input, loginStatus, size, page, sort);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{interviewQuestionId}/like")
    public ResponseEntity<Void> pressLike(@PathVariable final Long interviewQuestionId,
                                          @Extracted final LoginStatus loginStatus) {
        interviewQuestionService.pressLike(interviewQuestionId, loginStatus);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{interviewQuestionId}/like")
    public ResponseEntity<Void> cancelLike(@PathVariable final Long interviewQuestionId,
                                           @Extracted final LoginStatus loginStatus) {
        interviewQuestionService.cancelLike(interviewQuestionId, loginStatus);
        return ResponseEntity.noContent().build();
    }
}
