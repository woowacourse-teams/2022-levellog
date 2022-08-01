package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreQuestionService {

    private final PreQuestionRepository preQuestionRepository;
    private final LevellogRepository levellogRepository;
    private final MemberRepository memberRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Long save(final PreQuestionDto request, final Long levellogId, final Long memberId) {
        final Levellog levellog = getLevellog(levellogId);
        final Member from = getMember(memberId);

        validateIsAuthor(levellog, from);
        validateExistParticipantByMember(levellog.getTeam(), from);

        return preQuestionRepository.save(request.toEntity(levellog, from))
                .getId();
    }

    public PreQuestionDto findById(final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        final Levellog levellog = getLevellog(levellogId);
        final Member from = getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateFromMember(preQuestion, from);
        validateExistPreQuestion(levellog, from);

        return PreQuestionDto.from(preQuestion);
    }

    @Transactional
    public void update(final PreQuestionDto request, final Long preQuestionId, final Long levellogId,
                       final Long memberId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        final Levellog levellog = getLevellog(levellogId);
        final Member from = getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateFromMember(preQuestion, from);
        validateExistPreQuestion(levellog, from);

        preQuestion.update(request.getPreQuestion());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        final Levellog levellog = getLevellog(levellogId);
        final Member from = getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateFromMember(preQuestion, from);
        validateExistPreQuestion(levellog, from);

        preQuestionRepository.deleteById(preQuestion.getId());
    }

    private PreQuestion getPreQuestion(final Long preQuestionId) {
        return preQuestionRepository.findById(preQuestionId)
                .orElseThrow(() -> new PreQuestionNotFoundException(
                        "사전 질문이 존재하지 않습니다. preQuestionId : " + preQuestionId));
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException("레벨로그가 존재하지 않습니다. levellogId : " + levellogId));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private void validateLevellog(final PreQuestion preQuestion, final Levellog levellog) {
        if (!preQuestion.isSameLevellog(levellog)) {
            throw new InvalidFieldException(
                    "입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : " + levellog.getId());
        }
    }

    private void validateExistParticipantByMember(final Team team, final Member member) {
        final List<Participant> participants = participantRepository.findByTeam(team);

        if (!existsParticipantByMember(participants, member)) {
            throw new UnauthorizedException("같은 팀에 속한 멤버만 사전 질문을 작성할 수 있습니다.");
        }
    }

    private boolean existsParticipantByMember(final List<Participant> participants, final Member member) {
        return participants.stream()
                .anyMatch(participant -> participant.getMember().equals(member));
    }

    private void validateExistPreQuestion(final Levellog levellog, final Member member) {
        if (!preQuestionRepository.existsByLevellogAndFrom(levellog, member)) {
            throw new PreQuestionNotFoundException("아직 사전 질문을 작성하지 않았습니다.");
        }
    }

    private void validateIsAuthor(final Levellog levellog, final Member member) {
        if (levellog.isAuthor(member)) {
            throw new UnauthorizedException("자신의 레벨로그에는 사전 질문을 작성할 수 없습니다.");
        }
    }

    private void validateFromMember(final PreQuestion preQuestion, final Member member) {
        if (!preQuestion.getFrom().equals(member)) {
            throw new UnauthorizedException("자신의 사전 질문이 아닙니다.");
        }
    }
}
