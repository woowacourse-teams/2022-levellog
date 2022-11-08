package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface PreQuestionRepository extends JpaRepository<PreQuestion, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    boolean existsByLevellogAndAuthorId(Levellog levellog, Long authorId);

    default PreQuestion getPreQuestion(final Long preQuestionId) {
        return findById(preQuestionId)
                .orElseThrow(() -> new PreQuestionNotFoundException(DebugMessage.init()
                        .append("preQuestionId", preQuestionId)));
    }
}
