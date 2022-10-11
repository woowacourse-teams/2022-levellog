package com.woowacourse.levellog.team.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class TeamListResponse {

    private final List<TeamListDetailResponse> teams;

    public TeamListResponse(final List<TeamListDetailResponse> teams) {
        this.teams = teams;
    }
}
