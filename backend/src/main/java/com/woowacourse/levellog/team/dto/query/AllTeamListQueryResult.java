package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.dto.response.TeamListDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AllTeamListQueryResult {

    private final List<AllTeamListDetailQueryResult> results;

    public AllTeamListQueryResult(final List<AllTeamListDetailQueryResult> results) {
        this.results = results;
    }


    public TeamListResponse toResponse(final LocalDateTime now) {
        return results.stream()
                .collect(Collectors.groupingBy(
                        AllTeamListDetailQueryResult::getId,
                        LinkedHashMap::new,
                        Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamListDetailResponse.of(it, it.get(0).getTeamStatus(now)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), TeamListResponse::new));
    }
}
