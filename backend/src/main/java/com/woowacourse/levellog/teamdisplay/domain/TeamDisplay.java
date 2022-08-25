package com.woowacourse.levellog.teamdisplay.domain;

import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TeamDisplay {

    private final Team team;
    private final TeamStatus status;
    private final Participants units;
}
