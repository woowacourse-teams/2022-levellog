package com.woowacourse.levellog.interviewquestion.application;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionResponses;
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
    public Long save(final InterviewQuestionDto request, final Long levellogId, final Long fromMemberId) {
        final Member fromMember = getMember(fromMemberId);
        final Levellog levellog = getLevellog(levellogId);

        validateMemberIsParticipant(fromMember, levellog);
        levellog.getTeam()
                .validateInProgress(timeStandard.now(), "인터뷰 시작 전에 사전 질문을 작성 할 수 없습니다.");

        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(fromMember, levellog);

        return interviewQuestionRepository.save(interviewQuestion)
                .getId();
    }

    public InterviewQuestionResponses findAllByLevellog(final Long levellogId) {
        final Levellog levellog = getLevellog(levellogId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellog(levellog);

        return InterviewQuestionResponses.from(interviewQuestions);
    }

    public InterviewQuestionsDto findAllByLevellogAndAuthor(final Long levellogId, final Long fromMemberId) {
        final Levellog levellog = getLevellog(levellogId);
        final Member fromMember = getMember(fromMemberId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthor(
                levellog, fromMember);

        return InterviewQuestionsDto.from(interviewQuestions);
    }

    @Transactional
    public void update(final InterviewQuestionDto request, final Long interviewQuestionId, final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = getInterviewQuestion(interviewQuestionId);
        final Member fromMember = getMember(fromMemberId);

        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now(), "인터뷰 시작 전에 사전 질문을 수정 할 수 없습니다.");

        interviewQuestion.updateContent(request.getInterviewQuestion(), fromMember);
    }

    @Transactional
    public void deleteById(final Long interviewQuestionId, final Long fromMemberId) {
        final InterviewQuestion interviewQuestion = getInterviewQuestion(interviewQuestionId);
        final Member member = getMember(fromMemberId);

        interviewQuestion.validateMemberIsAuthor(member, "인터뷰 질문을 삭제할 수 있는 권한이 없습니다.");
        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now(), "인터뷰 시작 전에 사전 질문을 삭제 할 수 없습니다.");

        interviewQuestionRepository.delete(interviewQuestion);
    }

    private InterviewQuestion getInterviewQuestion(final Long interviewQuestionId) {
        return interviewQuestionRepository.findById(interviewQuestionId)
                .orElseThrow(() -> new InterviewQuestionNotFoundException(
                        "존재하지 않는 인터뷰 질문 [interviewQuestionId : " + interviewQuestionId + "]"));
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException("존재하지 않는 레벨로그 [levellogId : " + levellogId + "]"));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 멤버 [memberId : " + memberId + "]"));
    }

    private void validateMemberIsParticipant(final Member member, final Levellog levellog) {
        final Team team = levellog.getTeam();

        if (!participantRepository.existsByMemberAndTeam(member, team)) {
            throw new UnauthorizedException(
                    "같은 팀에 속한 멤버만 인터뷰 질문을 작성할 수 있습니다. [memberId :" + member.getId() + " teamId : " + team.getId()
                            + " levellogId : " + levellog.getId() + "]");
        }
    }
}
