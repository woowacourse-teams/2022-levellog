package com.woowacourse.levellog.team.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class TeamListResponses {

    private final List<TeamListResponse> teams;

    public TeamListResponses(final List<TeamListResponse> teams) {
        this.teams = teams;
    }
}
