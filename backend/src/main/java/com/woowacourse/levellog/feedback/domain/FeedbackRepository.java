package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByLevellog(Levellog levellog);

    List<Feedback> findAllByToOrderByUpdatedAtDesc(Member member);

    Optional<Feedback> findByLevellogIdAndFromId(Long levellogId, Long fromId);
}
