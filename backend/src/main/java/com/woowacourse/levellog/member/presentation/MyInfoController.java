package com.woowacourse.levellog.member.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.response.FeedbackListResponse;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.dto.response.LevellogListResponse;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.dto.request.NicknameUpdateRequest;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.dto.response.TeamListResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/my-info")
@RequiredArgsConstructor
public class MyInfoController {

    private final FeedbackService feedbackService;
    private final LevellogService levellogService;
    private final TeamQueryService teamQueryService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> myInfo(@Authentic final Long memberId) {
        final MemberResponse memberResponse = memberService.findMemberById(memberId);

        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbackListResponse> findAllFeedbackToMe(@Authentic final Long memberId) {
        final FeedbackListResponse feedbackListResponse = feedbackService.findAllByTo(memberId);

        return ResponseEntity.ok(feedbackListResponse);
    }

    @GetMapping("/levellogs")
    public ResponseEntity<LevellogListResponse> findAllMyLevellogs(@Authentic final Long memberId) {
        final LevellogListResponse levellogsResponse = levellogService.findAllByAuthorId(memberId);

        return ResponseEntity.ok(levellogsResponse);
    }

    @GetMapping("/teams")
    public ResponseEntity<TeamListResponse> findAllMyTeams(@Authentic final Long memberId) {
        final TeamListResponse response = teamQueryService.findAllByMemberId(memberId);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Void> updateNickname(@Authentic final Long memberId,
                                               @RequestBody @Valid final NicknameUpdateRequest request) {
        memberService.updateNickname(request, memberId);

        return ResponseEntity.noContent().build();
    }
}
