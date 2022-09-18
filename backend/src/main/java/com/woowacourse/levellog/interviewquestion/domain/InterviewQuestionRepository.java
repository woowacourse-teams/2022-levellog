package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findAllByLevellog(Levellog levellog);

    List<InterviewQuestion> findAllByLevellogAndAuthor(Levellog levellog, Member member);

    List<InterviewQuestion> findAllByContentContains(String content, Pageable pageable);
}
