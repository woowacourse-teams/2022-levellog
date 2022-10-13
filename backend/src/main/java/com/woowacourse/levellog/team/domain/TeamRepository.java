package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    default Team getTeam(final Long teamId) {
        return findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(DebugMessage.init()
                        .append("teamId", teamId)));
    }
}
