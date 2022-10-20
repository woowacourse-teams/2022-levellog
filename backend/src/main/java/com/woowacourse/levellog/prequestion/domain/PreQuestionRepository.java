package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PreQuestionRepository extends JpaRepository<PreQuestion, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByLevellogAndAuthorId(Levellog levellog, Long authorId);

    default PreQuestion getPreQuestion(final Long preQuestionId) {
        return findById(preQuestionId)
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("preQuestionId", preQuestionId)));
    }
}
