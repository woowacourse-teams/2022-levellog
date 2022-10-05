package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.dto.query.AllTeamListQueryResult;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamListResponses {

    private List<TeamListResponse> teams;

    public static TeamListResponses from(final List<AllTeamListQueryResult> allParticipants, final LocalDateTime time) {
        final List<TeamListResponse> responses = allParticipants.stream()
                .collect(Collectors.groupingBy(
                        AllTeamListQueryResult::getId,
                        LinkedHashMap::new,
                        Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamListResponse.of(it, it.get(0).getTeamStatus(time)))
                .collect(Collectors.toList());

        return new TeamListResponses(responses);
    }
}
