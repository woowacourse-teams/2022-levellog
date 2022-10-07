package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    @Query("SELECT i FROM InterviewQuestion i INNER JOIN FETCH i.author WHERE i.levellog = :levellog ORDER BY i.createdAt")
    List<InterviewQuestion> findAllByLevellog(@Param("levellog") Levellog levellog);

    List<InterviewQuestion> findAllByLevellogAndAuthor(Levellog levellog, Member member);

    default InterviewQuestion getInterviewQuestion(final Long interviewQuestionId) {
        return findById(interviewQuestionId)
                .orElseThrow(() -> new InterviewQuestionNotFoundException(DebugMessage.init()
                        .append("interviewQuestionId", interviewQuestionId)));
    }
}
