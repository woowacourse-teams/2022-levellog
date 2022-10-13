package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionQueryRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.prequestion.dto.request.PreQuestionWriteRequest;
import com.woowacourse.levellog.prequestion.dto.response.PreQuestionResponse;
import com.woowacourse.levellog.prequestion.exception.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
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

    @Transactional
    public Long save(final PreQuestionWriteRequest request, final Long levellogId,
                     @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        levellog.getTeam().validateIsParticipants(loginStatus.getMemberId());

        validatePreQuestionExistence(levellog, loginStatus.getMemberId());

        return preQuestionRepository.save(request.toEntity(levellog, loginStatus.getMemberId()))
                .getId();
    }

    public PreQuestionResponse findMy(final Long levellogId, @Verified final LoginStatus loginStatus) {
        levellogRepository.getLevellog(levellogId);

        return preQuestionQueryRepository.findByLevellogAndAuthor(levellogId, loginStatus.getMemberId())
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)
                        .append("memberId", loginStatus.getMemberId())));
    }

    @Transactional
    public void update(final PreQuestionWriteRequest request, final Long preQuestionId, final Long levellogId,
                       @Verified final LoginStatus loginStatus) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, loginStatus.getMemberId());

        preQuestion.update(request.getContent());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, @Verified final LoginStatus loginStatus) {
        final PreQuestion preQuestion = preQuestionRepository.getPreQuestion(preQuestionId);
        final Levellog levellog = levellogRepository.getLevellog(levellogId);

        validateLevellog(preQuestion, levellog);
        validateMyQuestion(preQuestion, loginStatus.getMemberId());

        preQuestionRepository.deleteById(preQuestion.getId());
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
