package com.woowacourse.levellog.member.presentation;

import com.woowacourse.levellog.authentication.support.Extracted;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponses;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.dto.request.NicknameUpdateRequest;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
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
    public ResponseEntity<MemberResponse> myInfo(@Extracted final LoginStatus loginStatus) {
        final MemberResponse memberResponse = memberService.findMemberById(loginStatus);

        return ResponseEntity.ok(memberResponse);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbackResponses> findAllFeedbackToMe(@Extracted final LoginStatus loginStatus) {
        final FeedbackResponses feedbackResponses = feedbackService.findAllByTo(loginStatus);

        return ResponseEntity.ok(feedbackResponses);
    }

    @GetMapping("/levellogs")
    public ResponseEntity<LevellogResponses> findAllMyLevellogs(@Extracted final LoginStatus loginStatus) {
        final LevellogResponses levellogsResponse = levellogService.findAllByAuthorId(loginStatus);

        return ResponseEntity.ok(levellogsResponse);
    }

    @GetMapping("/teams")
    public ResponseEntity<TeamListResponses> findAllMyTeams(@Extracted final LoginStatus loginStatus) {
        final TeamListResponses response = teamQueryService.findAllByMemberId(loginStatus);

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Void> updateNickname(@Extracted final LoginStatus loginStatus,
                                               @RequestBody @Valid final NicknameUpdateRequest request) {
        memberService.updateNickname(request, loginStatus);

        return ResponseEntity.noContent().build();
    }
}
