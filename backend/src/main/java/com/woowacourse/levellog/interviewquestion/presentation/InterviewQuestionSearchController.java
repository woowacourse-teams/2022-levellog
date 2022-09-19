package com.woowacourse.levellog.interviewquestion.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.PublicAPI;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultsDto;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<InterviewQuestionSearchResultsDto> searchBy(
            @RequestParam(required = false) final String keyword,
            final Pageable pageable,
            @Authentic final Long memberId) {
        if (keyword == null) {
            return ResponseEntity.ok(InterviewQuestionSearchResultsDto.of(new ArrayList<>()));
        }
        final InterviewQuestionSearchResultsDto response = interviewQuestionService
                .searchByKeyword(keyword, pageable, memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{interviewQuestionId}/like")
    public ResponseEntity<Void> pressLike(@PathVariable final Long interviewQuestionId,
                                          @Authentic final Long memberId) {
        interviewQuestionService.pressLike(interviewQuestionId, memberId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{interviewQuestionId}/like")
    public ResponseEntity<Void> cancelLike(@PathVariable final Long interviewQuestionId,
                                           @Authentic final Long memberId) {
        interviewQuestionService.cancelLike(interviewQuestionId, memberId);
        return ResponseEntity.noContent().build();
    }
}
