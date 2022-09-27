package com.woowacourse.levellog.interviewquestion.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikesRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionQueryRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionSort;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionWriteDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionsDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikeNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikesAlreadyExistException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewQuestionQueryRepository interviewQuestionQueryRepository;
    private final InterviewQuestionLikesRepository interviewQuestionLikesRepository;
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;
    private final ParticipantRepository participantRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final InterviewQuestionWriteDto request, final Long levellogId, final Long fromMemberId) {
        final Member author = memberRepository.getMember(fromMemberId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfInterviewQuestion(author);
        validateMemberIsParticipant(author, levellog);
        team.validateInProgress(timeStandard.now());

        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(author, levellog);

        return interviewQuestionRepository.save(interviewQuestion)
                .getId();
    }

    public InterviewQuestionsDto findAllByLevellog(final Long levellogId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellog(levellog);

        return InterviewQuestionsDto.from(interviewQuestions);
    }

    public InterviewQuestionContentsDto findAllByLevellogAndAuthor(final Long levellogId, final Long fromMemberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member author = memberRepository.getMember(fromMemberId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthor(
                levellog, author);

        return InterviewQuestionContentsDto.from(interviewQuestions);
    }

    public InterviewQuestionSearchResultsDto searchByKeyword(final String keyword, final Long memberId,
                                                             final Long size, final Long page, final String sort) {
        final List<InterviewQuestionSearchResultDto> results = interviewQuestionQueryRepository
                .searchByKeyword(keyword, memberId, size, page, InterviewQuestionSort.valueOf(sort.toUpperCase()));

        return InterviewQuestionSearchResultsDto.of(results, page);
    }

    @Transactional
    public void update(final InterviewQuestionWriteDto request, final Long interviewQuestionId,
                       final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        final Member author = memberRepository.getMember(fromMemberId);

        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestion.updateContent(request.getContent(), author);
    }

    @Transactional
    public void deleteById(final Long interviewQuestionId, final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        final Member author = memberRepository.getMember(fromMemberId);

        interviewQuestion.validateMemberIsAuthor(author);
        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestionRepository.delete(interviewQuestion);
    }

    @Transactional
    public void pressLike(final Long interviewQuestionId, final Long memberId) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        final Member member = memberRepository.getMember(memberId);
        validateAlreadyExist(interviewQuestionId, memberId);

        interviewQuestionLikesRepository.save(InterviewQuestionLikes.of(interviewQuestion, member));
        interviewQuestion.upLike();
    }

    @Transactional
    public void cancelLike(final Long interviewQuestionId, final Long memberId) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        final Member member = memberRepository.getMember(memberId);
        final InterviewQuestionLikes interviewQuestionLikes = getInterviewQuestionLikes(interviewQuestion, member);

        interviewQuestionLikesRepository.deleteById(interviewQuestionLikes.getId());
        interviewQuestion.downLike();
    }

    private InterviewQuestionLikes getInterviewQuestionLikes(final InterviewQuestion interviewQuestion,
                                                             final Member member) {
        return interviewQuestionLikesRepository.findByInterviewQuestionIdAndLikerId(interviewQuestion.getId(),
                        member.getId())
                .orElseThrow(() -> new InterviewQuestionLikeNotFoundException(
                        DebugMessage.init()
                                .append("interviewQuestionId", interviewQuestion.getId())
                                .append("likerId", member.getId())
                ));
    }

    private void validateMemberIsParticipant(final Member member, final Levellog levellog) {
        final Team team = levellog.getTeam();

        if (!participantRepository.existsByMemberAndTeam(member, team)) {
            throw new ParticipantNotSameTeamException(DebugMessage.init()
                    .append("teamId", team.getId())
                    .append("memberId", member.getId())
                    .append("levellogId", levellog.getId()));
        }
    }

    private void validateAlreadyExist(final Long interviewQuestionId, final Long memberId) {
        if (interviewQuestionLikesRepository.existsByInterviewQuestionIdAndLikerId(interviewQuestionId, memberId)) {
            final DebugMessage debugMessage = DebugMessage.init()
                    .append("interviewQuestionId", interviewQuestionId)
                    .append("likerId", memberId);
            throw new InterviewQuestionLikesAlreadyExistException(debugMessage);
        }
    }
}
