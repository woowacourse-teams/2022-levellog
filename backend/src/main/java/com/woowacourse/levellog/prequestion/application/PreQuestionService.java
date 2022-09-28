package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.dto.PreQuestionWriteDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionAlreadyExistException;
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
    public Long save(final PreQuestionWriteDto request, final Long levellogId, final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member questioner = memberRepository.getMember(memberId);

        validatePreQuestionExistence(levellog, questioner);
        validateSameTeamMember(levellog.getTeam(), questioner);

        return preQuestionRepository.save(request.toEntity(levellog, questioner))
                .getId();
    }

    public PreQuestionDto findMy(final Long levellogId, final Long questionerId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member questioner = memberRepository.getMember(questionerId);
        final PreQuestion preQuestion = preQuestionRepository.findByLevellogAndAuthor(levellog, questioner)
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)
                        .append("memberId", questionerId)));

        return PreQuestionDto.from(questioner, preQuestion.getContent());
    }

    @Transactional
    public void update(final PreQuestionWriteDto request, final Long preQuestionId, final Long levellogId,
                       final Long memberId) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member questioner = memberRepository.getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, questioner);

        preQuestion.update(request.getContent());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member questioner = memberRepository.getMember(memberId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, questioner);

        preQuestionRepository.deleteById(preQuestion.getId());
    }

    private void validateSameTeamMember(final Team team, final Member member) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        participants.validateExistsMember(member);
    }

    private void validatePreQuestionExistence(final Levellog levellog, final Member questioner) {
        final boolean isExists = preQuestionRepository.existsByLevellogAndAuthor(levellog, questioner);
        if (isExists) {
            throw new PreQuestionAlreadyExistException(DebugMessage.init()
                    .append("levellogId", levellog.getId())
                    .append("authorId", questioner.getId()));
        }
    }

    private void validateLevellog(final PreQuestion preQuestion, final Levellog levellog) {
        preQuestion.validateLevellog(levellog);
    }

    private void validateMyQuestion(final PreQuestion preQuestion, final Member member) {
        preQuestion.validateMyQuestion(member);
    }
}
