package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.prequestion.dto.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
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
        final Member questioner = getMember(memberId);

        validatePreQuestionExistence(levellog, questioner);
        validateSameTeamMember(levellog.getTeam(), questioner);

        return preQuestionRepository.save(request.toEntity(levellog, questioner))
                .getId();
    }

    public PreQuestionDto findMy(final Long levellogId, final Long questionerId) {
        final Levellog levellog = getLevellog(levellogId);
        final Member questioner = getMember(questionerId);
        final PreQuestion preQuestion = preQuestionRepository.findByLevellogAndAuthor(levellog, questioner)
                .orElseThrow(() -> new PreQuestionNotFoundException("사전 질문이 존재하지 않습니다. "
                        + "[ levellogId : " + levellogId + " memberId : " + questionerId + " ]"));

        return PreQuestionDto.from(preQuestion.getContent());
    }

    @Transactional
    public void update(final PreQuestionDto request, final Long preQuestionId, final Long levellogId,
                       final Long memberId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        final Levellog levellog = getLevellog(levellogId);
        final Member questioner = getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, questioner);

        preQuestion.update(request.getPreQuestion());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        final Levellog levellog = getLevellog(levellogId);
        final Member questioner = getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, questioner);

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

    private void validateSameTeamMember(final Team team, final Member member) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        participants.validateExistsMember(member);
    }

    private void validatePreQuestionExistence(final Levellog levellog, final Member questioner) {
        final boolean isExists = preQuestionRepository.existsByLevellogAndAuthor(levellog, questioner);
        if (isExists) {
            throw new PreQuestionAlreadyExistException(
                    "사전 질문을 이미 작성하였습니다. levellogId : " + levellog.getId() + " authorId : " + questioner.getId());
        }
    }

    private void validateLevellog(final PreQuestion preQuestion, final Levellog levellog) {
        preQuestion.validateLevellog(levellog);
    }

    private void validateMyQuestion(final PreQuestion preQuestion, final Member member) {
        preQuestion.validateMyQuestion(member);
    }
}
