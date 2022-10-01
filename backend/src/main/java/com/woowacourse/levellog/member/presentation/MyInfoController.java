package com.woowacourse.levellog.member.presentation;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.authentication.support.FromToken;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.dto.LevellogsDto;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.dto.TeamListDto;
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
    public ResponseEntity<MemberDto> myInfo(@FromToken final LoginStatus loginStatus) {
        final MemberDto memberDto = memberService.findMemberById(loginStatus);

        return ResponseEntity.ok(memberDto);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbacksDto> findAllFeedbackToMe(@FromToken final LoginStatus loginStatus) {
        final FeedbacksDto feedbacksResponse = feedbackService.findAllByTo(loginStatus);

        return ResponseEntity.ok(feedbacksResponse);
    }

    @GetMapping("/levellogs")
    public ResponseEntity<LevellogsDto> findAllMyLevellogs(@FromToken final LoginStatus loginStatus) {
        final LevellogsDto levellogsResponse = levellogService.findAllByAuthorId(loginStatus);

        return ResponseEntity.ok(levellogsResponse);
    }

    @GetMapping("/teams")
    public ResponseEntity<TeamListDto> findAllMyTeams(@FromToken final LoginStatus loginStatus) {
        final TeamListDto teamsDto = teamQueryService.findAllByMemberId(loginStatus);

        return ResponseEntity.ok(teamsDto);
    }

    @PutMapping
    public ResponseEntity<Void> updateNickname(@FromToken final LoginStatus loginStatus,
                                               @RequestBody @Valid final NicknameUpdateDto request) {
        memberService.updateNickname(request, loginStatus);

        return ResponseEntity.noContent().build();
    }
}
