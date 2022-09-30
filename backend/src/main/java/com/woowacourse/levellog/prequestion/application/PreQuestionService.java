package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionQueryRepository;
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
    private final PreQuestionQueryRepository preQuestionQueryRepository;
    private final LevellogRepository levellogRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Long save(final PreQuestionWriteDto request, final Long levellogId, @Authentic final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validatePreQuestionExistence(levellog, memberId);
        validateSameTeamMember(levellog.getTeam(), memberId);

        return preQuestionRepository.save(request.toEntity(levellog, memberId))
                .getId();
    }

    public PreQuestionDto findMy(final Long levellogId, @Authentic final Long questionerId) {
        levellogRepository.getLevellog(levellogId);

        return preQuestionQueryRepository.findByLevellogAndAuthor(levellogId, questionerId)
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)
                        .append("memberId", questionerId)));
    }

    @Transactional
    public void update(final PreQuestionWriteDto request, final Long preQuestionId, final Long levellogId,
                       @Authentic final Long memberId) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, memberId);

        preQuestion.update(request.getContent());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, @Authentic final Long memberId) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, memberId);

        preQuestionRepository.deleteById(preQuestion.getId());
    }

    private void validateSameTeamMember(final Team team, final Long memberId) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        participants.validateExistsMember(memberId);
    }

    private void validatePreQuestionExistence(final Levellog levellog, final Long questionerId) {
        final boolean isExists = preQuestionRepository.existsByLevellogAndAuthorId(levellog, questionerId);
        if (isExists) {
            throw new PreQuestionAlreadyExistException(DebugMessage.init()
                    .append("levellogId", levellog.getId())
                    .append("authorId", questionerId));
        }
    }

    private void validateLevellog(final PreQuestion preQuestion, final Levellog levellog) {
        preQuestion.validateLevellog(levellog);
    }

    private void validateMyQuestion(final PreQuestion preQuestion, final Long memberId) {
        preQuestion.validateMyQuestion(memberId);
    }
}
