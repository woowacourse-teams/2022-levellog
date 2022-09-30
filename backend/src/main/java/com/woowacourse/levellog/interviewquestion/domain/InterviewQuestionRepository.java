package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findAllByLevellogAndAuthorId(Levellog levellog, Long authorId);

    default InterviewQuestion getInterviewQuestion(final Long interviewQuestionId) {
        return findById(interviewQuestionId)
                .orElseThrow(() -> new InterviewQuestionNotFoundException(DebugMessage.init()
                        .append("interviewQuestionId", interviewQuestionId)));
    }
}
