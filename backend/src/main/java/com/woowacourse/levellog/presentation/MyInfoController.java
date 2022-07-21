package com.woowacourse.levellog.presentation;

import com.woowacourse.levellog.application.FeedbackService;
import com.woowacourse.levellog.application.MemberService;
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import com.woowacourse.levellog.dto.MemberResponse;
import com.woowacourse.levellog.dto.NicknameUpdateDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/myInfo")
@RequiredArgsConstructor
public class MyInfoController {

    private final FeedbackService feedbackService;
    private final MemberService memberService;

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbacksResponse> findAllFeedbackToMe(@LoginMember final Long memberId) {
        final FeedbacksResponse feedbacksResponse = feedbackService.findAllByTo(memberId);
        return ResponseEntity.ok(feedbacksResponse);
    }

    @GetMapping
    public ResponseEntity<MemberResponse> myInfo(@LoginMember final Long memberId) {
        final MemberResponse memberResponse = memberService.findMemberById(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateNickname(@LoginMember final Long memberId,
                                               @RequestBody @Valid final NicknameUpdateDto nicknameUpdateDto) {
        memberService.updateNickname(memberId, nicknameUpdateDto);
        return ResponseEntity.noContent().build();
    }
}
