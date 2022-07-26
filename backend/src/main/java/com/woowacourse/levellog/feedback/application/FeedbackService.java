package com.woowacourse.levellog.feedback.application;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackRequest;
import com.woowacourse.levellog.feedback.dto.FeedbackResponse;
import com.woowacourse.levellog.feedback.dto.FeedbacksResponse;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
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

    @Transactional
    public Long save(final FeedbackRequest request, final Long levellogId, final Long fromMemberId) {
        validateExistence(levellogId, fromMemberId);

        final Member member = getMember(fromMemberId);
        final FeedbackContentDto feedbackContent = request.getFeedback();
        final Levellog levellog = getLevellog(levellogId);

        validateTeamMember(levellogId, member);
        validateAuthor(member, levellog);

        final Feedback feedback = new Feedback(
                member,
                levellog.getAuthor(),
                levellog,
                feedbackContent.getStudy(),
                feedbackContent.getSpeak(),
                feedbackContent.getEtc());

        return feedbackRepository.save(feedback).getId();
    }

    public FeedbacksResponse findAll(final Long levellogId) {
        final Levellog levellog = getLevellog(levellogId);
        final List<FeedbackResponse> responses = getFeedbackResponses(feedbackRepository.findAllByLevellog(levellog));

        return new FeedbacksResponse(responses);
    }

    public FeedbacksResponse findAllByTo(final Long memberId) {
        final Member member = getMember(memberId);
        final List<Feedback> feedbacks = feedbackRepository.findAllByToOrderByUpdatedAtDesc(member);

        return new FeedbacksResponse(getFeedbackResponses(feedbacks));
    }

    @Transactional
    public void update(final FeedbackRequest request, final Long feedbackId) {
        final Feedback feedback = getFeedback(feedbackId);

        feedback.updateFeedback(
                request.getFeedback().getStudy(),
                request.getFeedback().getSpeak(),
                request.getFeedback().getEtc());
    }

    @Transactional
    public void deleteById(final Long feedbackId, final Long memberId) {
        final Feedback feedback = getFeedback(feedbackId);
        final Member member = getMember(memberId);
        validateAssociatedMember(feedback, member);

        feedbackRepository.deleteById(feedbackId);
    }

    private void validateExistence(Long levellogId, Long fromMemberId) {
        if (feedbackRepository.existsByLevellogIdAndFromId(levellogId, fromMemberId)) {
            throw new FeedbackAlreadyExistException(levellogId);
        }
    }

    private void validateTeamMember(final Long levellogId, final Member member) {
        final Team team = getTeam(levellogId);

        if (!participantRepository.existsByMemberAndTeam(member, team)) {
            throw new InvalidFeedbackException("같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다.");
        }
    }

    private void validateAuthor(Member member, Levellog levellog) {
        if (levellog.getAuthor().equals(member)) {
            throw new InvalidFeedbackException("자기 자신에게 피드백을 할 수 없습니다.");
        }
    }

    private void validateAssociatedMember(Feedback feedback, Member member) {
        if (!feedback.isAssociatedWith(member)) {
            throw new InvalidFeedbackException("자신이 남기거나 받은 피드백만 삭제할 수 있습니다.");
        }
    }

    private List<FeedbackResponse> getFeedbackResponses(final List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackResponse::from)
                .collect(Collectors.toList());
    }

    private Team getTeam(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .map(Levellog::getTeam)
                .orElseThrow(LevellogNotFoundException::new);
    }

    private Member getMember(final Long memberId) {
        return memberRepository
                .findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(LevellogNotFoundException::new);
    }

    private Feedback getFeedback(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
    }
}
