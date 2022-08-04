package com.woowacourse.levellog.member.presentation;

<<<<<<< HEAD
import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.dto.LevellogsDto;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.dto.TeamsDto;
=======
import com.woowacourse.levellog.authentication.support.LoginMember;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.dto.FeedbacksResponse;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.dto.MemberResponse;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
>>>>>>> main
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
<<<<<<< HEAD
@RequestMapping("/api/my-info")
=======
@RequestMapping("/api/myInfo")
>>>>>>> main
@RequiredArgsConstructor
public class MyInfoController {

    private final FeedbackService feedbackService;
<<<<<<< HEAD
    private final LevellogService levellogService;
    private final TeamService teamService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberDto> myInfo(@Authentic final Long memberId) {
        final MemberDto memberDto = memberService.findMemberById(memberId);

        return ResponseEntity.ok(memberDto);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<FeedbacksDto> findAllFeedbackToMe(@Authentic final Long memberId) {
        final FeedbacksDto feedbacksResponse = feedbackService.findAllByTo(memberId);

        return ResponseEntity.ok(feedbacksResponse);
    }

    @GetMapping("/levellogs")
    public ResponseEntity<LevellogsDto> findAllMyLevellogs(@Authentic final Long memberId) {
        final LevellogsDto levellogsResponse = levellogService.findAllByAuthorId(memberId);

        return ResponseEntity.ok(levellogsResponse);
    }

    @GetMapping("/teams")
    public ResponseEntity<TeamsDto> findAllMyTeams(@Authentic final Long memberId) {
        final TeamsDto teamsDto = teamService.findAllByMemberId(memberId);

        return ResponseEntity.ok(teamsDto);
    }

    @PutMapping
    public ResponseEntity<Void> updateNickname(@Authentic final Long memberId,
                                               @RequestBody @Valid final NicknameUpdateDto request) {
        memberService.updateNickname(request, memberId);

=======
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
>>>>>>> main
        return ResponseEntity.noContent().build();
    }
}
