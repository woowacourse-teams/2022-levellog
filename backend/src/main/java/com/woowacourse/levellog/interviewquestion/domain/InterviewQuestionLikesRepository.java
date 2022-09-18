package com.woowacourse.levellog.interviewquestion.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewQuestionLikesRepository extends JpaRepository<InterviewQuestionLikes, Long> {
    boolean existsByInterviewQuestionIdAndLikerId(Long interviewQuestionId, Long likerId);

    Optional<InterviewQuestionLikes> findByInterviewQuestionIdAndLikerId(Long interviewQuestionId, Long likerId);
}
