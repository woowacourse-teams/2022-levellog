package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByLevellogIdAndFromId(Long levellogId, Long fromId);

    default Feedback getFeedback(final Long feedbackId) {
        return findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException(DebugMessage.init()
                        .append("feedbackId", feedbackId)));
    }
}
