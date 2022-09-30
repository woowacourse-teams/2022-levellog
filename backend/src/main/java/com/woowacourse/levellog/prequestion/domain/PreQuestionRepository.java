package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreQuestionRepository extends JpaRepository<PreQuestion, Long> {

    boolean existsByLevellogAndAuthorId(Levellog levellog, Long authorId);

    default PreQuestion getPreQuestion(final Long preQuestionId) {
        return findById(preQuestionId)
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("preQuestionId", preQuestionId)));
    }
}
