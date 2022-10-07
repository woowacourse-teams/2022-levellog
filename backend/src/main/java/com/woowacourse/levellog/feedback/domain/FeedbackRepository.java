package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.FeedbackNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT f FROM Feedback f INNER JOIN FETCH f.from WHERE f.levellog = :levellog")
    List<Feedback> findAllByLevellog(@Param("levellog") Levellog levellog);

    @Query("SELECT f FROM Feedback f INNER JOIN FETCH f.from WHERE f.to = :member ORDER BY f.updatedAt DESC")
    List<Feedback> findAllByToOrderByUpdatedAtDesc(@Param("member") Member member);

    boolean existsByLevellogIdAndFromId(Long levellogId, Long fromId);

    default Feedback getFeedback(final Long feedbackId) {
        return findById(feedbackId)
                .orElseThrow(() -> new FeedbackNotFoundException(DebugMessage.init()
                        .append("feedbackId", feedbackId)));
    }
}
