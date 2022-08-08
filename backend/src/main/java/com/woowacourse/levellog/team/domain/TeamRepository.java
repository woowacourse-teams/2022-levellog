package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "SELECT t "
            + "FROM Team t "
            + "INNER JOIN Levellog l ON t = l.team "
            + "INNER JOIN InterviewQuestion iq ON l = iq.levellog "
            + "WHERE iq = :interviewQuestion")
    Optional<Team> findByInterviewQuestion(@Param("interviewQuestion") final InterviewQuestion interviewQuestion);
}
