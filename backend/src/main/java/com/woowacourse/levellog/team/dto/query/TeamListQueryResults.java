package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.dto.response.TeamListResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TeamListQueryResults {

    private final List<TeamListQueryResult> results;

    public TeamListQueryResults(final List<TeamListQueryResult> results) {
        this.results = results;
    }

    public TeamListResponses toResponse(final LocalDateTime now) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        TeamListQueryResult::getId,
                        LinkedHashMap::new,
                        Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamListResponse.of(it, it.get(0).getTeamStatus(now)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), TeamListResponses::new));
    }
}
