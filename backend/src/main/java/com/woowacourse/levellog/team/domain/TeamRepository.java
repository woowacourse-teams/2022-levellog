package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {

    default Team getTeam(final Long teamId) {
        return findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(DebugMessage.init()
                        .append("teamId", teamId)));
    }

    default Team getTeamWithParticipants(final Long teamId) {
        return findTeamWithParticipants(teamId)
                .orElseThrow(() -> new TeamNotFoundException(DebugMessage.init()
                        .append("teamId", teamId)));
    }

    @Query("SELECT t FROM Team t JOIN FETCH t.participants p")
    Optional<Team> findTeamWithParticipants(Long teamId);
}
