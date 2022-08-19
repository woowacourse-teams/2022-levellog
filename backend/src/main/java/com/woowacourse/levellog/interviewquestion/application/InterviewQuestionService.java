package com.woowacourse.levellog.interviewquestion.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionWriteDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionsDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
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
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;
    private final ParticipantRepository participantRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final InterviewQuestionWriteDto request, final Long levellogId, final Long fromMemberId) {
        final Member author = getMember(fromMemberId);
        final Levellog levellog = getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfInterviewQuestion(author);
        validateMemberIsParticipant(author, levellog);
        team.validateInProgress(timeStandard.now());

        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(author, levellog);

        return interviewQuestionRepository.save(interviewQuestion)
                .getId();
    }

    public InterviewQuestionsDto findAllByLevellog(final Long levellogId) {
        final Levellog levellog = getLevellog(levellogId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellog(levellog);

        return InterviewQuestionsDto.from(interviewQuestions);
    }

    public InterviewQuestionContentsDto findAllByLevellogAndAuthor(final Long levellogId, final Long fromMemberId) {
        final Levellog levellog = getLevellog(levellogId);
        final Member author = getMember(fromMemberId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthor(
                levellog, author);

        return InterviewQuestionContentsDto.from(interviewQuestions);
    }

    @Transactional
    public void update(final InterviewQuestionWriteDto request, final Long interviewQuestionId,
                       final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = getInterviewQuestion(interviewQuestionId);
        final Member author = getMember(fromMemberId);

        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestion.updateContent(request.getInterviewQuestion(), author);
    }

    @Transactional
    public void deleteById(final Long interviewQuestionId, final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = getInterviewQuestion(interviewQuestionId);
        final Member author = getMember(fromMemberId);

        interviewQuestion.validateMemberIsAuthor(author, "인터뷰 질문을 삭제할 수 있는 권한이 없습니다.");
        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestionRepository.delete(interviewQuestion);
    }

    private InterviewQuestion getInterviewQuestion(final Long interviewQuestionId) {
        return interviewQuestionRepository.findById(interviewQuestionId)
                .orElseThrow(() -> new InterviewQuestionNotFoundException(DebugMessage.init()
                        .append("interviewQuestionId", interviewQuestionId)));
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(DebugMessage.init()
                        .append("memberId", memberId)));
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
}
