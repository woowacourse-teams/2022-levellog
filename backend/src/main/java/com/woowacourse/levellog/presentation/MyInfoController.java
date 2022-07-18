package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.FeedbackService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/myInfo")
@RequiredArgsConstructor
public class MyInfoController {

    private final FeedbackService feedbackService;

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbacksResponse> findAllFeedbackToMe(@LoginMember final Member member) {
        final FeedbacksResponse feedbacksResponse = feedbackService.findAllByTo(member);
        return ResponseEntity.ok(feedbacksResponse);
    }
}
