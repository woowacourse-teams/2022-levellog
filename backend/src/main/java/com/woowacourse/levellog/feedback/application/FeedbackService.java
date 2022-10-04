package com.woowacourse.levellog.feedback.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.feedback.dto.request.FeedbackWriteRequest;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponse;
import com.woowacourse.levellog.feedback.dto.response.FeedbackListResponses;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final LevellogRepository levellogRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final FeedbackWriteRequest request, final Long levellogId, final Long fromMemberId) {
        validateExistence(levellogId, fromMemberId);

        final Member member = memberRepository.getMember(fromMemberId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfFeedback(member);
        validateTeamMember(team, member);
        team.validateInProgress(timeStandard.now());

        final Feedback feedback = request.getFeedback()
                .toFeedback(member, levellog);

        return feedbackRepository.save(feedback)
                .getId();
    }

    public FeedbackListResponses findAll(final Long levellogId, final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        validateTeamMember(levellog.getTeam(), memberRepository.getMember(memberId));

        final List<FeedbackResponse> responses = getFeedbackResponses(feedbackRepository.findAllByLevellog(levellog));

        return new FeedbackListResponses(responses);
    }

    public FeedbackResponse findById(final Long levellogId, final Long feedbackId, final Long memberId) {
        final Feedback feedback = feedbackRepository.getFeedback(feedbackId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member member = memberRepository.getMember(memberId);

        validateTeamMember(levellog.getTeam(), member);
        feedback.validateLevellog(levellog);

        return FeedbackResponse.from(feedback);
    }

    public FeedbackListResponses findAllByTo(final Long memberId) {
        final Member member = memberRepository.getMember(memberId);
        final List<Feedback> feedbacks = feedbackRepository.findAllByToOrderByUpdatedAtDesc(member);

        return new FeedbackListResponses(getFeedbackResponses(feedbacks));
    }

    @Transactional
    public void update(final FeedbackWriteRequest request, final Long feedbackId, final Long memberId) {
        final Feedback feedback = feedbackRepository.getFeedback(feedbackId);
        final Member member = memberRepository.getMember(memberId);
        final Team team = feedback.getLevellog().getTeam();

        feedback.validateAuthor(member);
        team.validateInProgress(timeStandard.now());

        feedback.updateFeedback(
                request.getFeedback().getStudy(),
                request.getFeedback().getSpeak(),
                request.getFeedback().getEtc());
    }

    private void validateExistence(final Long levellogId, final Long fromMemberId) {
        if (feedbackRepository.existsByLevellogIdAndFromId(levellogId, fromMemberId)) {
            throw new FeedbackAlreadyExistException(DebugMessage.init()
                    .append("levellogId", levellogId));
        }
    }

    private void validateTeamMember(final Team team, final Member member) {
        if (!participantRepository.existsByMemberAndTeam(member, team)) {
            throw new ParticipantNotSameTeamException(DebugMessage.init()
                    .append("teamId", team.getId())
                    .append("memberId", member.getId()));
        }
    }

    private List<FeedbackResponse> getFeedbackResponses(final List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponse::from)
                .collect(Collectors.toList());
    }
}
