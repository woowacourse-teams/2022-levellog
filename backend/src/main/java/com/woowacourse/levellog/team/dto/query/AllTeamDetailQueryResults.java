package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.response.ParticipantResponse;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamResponse;
import com.woowacourse.levellog.team.dto.response.WatcherResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AllTeamDetailQueryResults {

    List<AllTeamDetailQueryResult> results;

    public AllTeamDetailQueryResults(final List<AllTeamDetailQueryResult> results) {
        this.results = results;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public TeamDetailResponse toResponses(final LocalDateTime nowTime) {
        final AllTeamDetailQueryResult result = results.get(0);

        final TeamResponse teamResponse = result.getTeamResponse();
        final TeamStatus status = toTeamStatus(result, nowTime);
        final Long memberId = result.getParticipantResponse().getMemberId();
        final SimpleParticipants participants = toSimpleParticipants(results);

        return TeamDetailResponse.from2(
                teamResponse,
                status,
                memberId,
                participants,
                toParticipantResponses(results),
                toWatcherResponses(results)
        );
    }

    private SimpleParticipants toSimpleParticipants(final List<AllTeamDetailQueryResult> filteredParticipants) {
        final List<SimpleParticipant> participants = filteredParticipants.stream()
                .map(AllTeamDetailQueryResult::getSimpleParticipant)
                .collect(Collectors.toList());
        return new SimpleParticipants(participants);
    }

    private TeamStatus toTeamStatus(final AllTeamDetailQueryResult result, final LocalDateTime nowTime) {
        return result.getAllTeamListQueryResult()
                .getTeamStatus(nowTime);
    }

    private List<ParticipantResponse> toParticipantResponses(
            final List<AllTeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> !it.getSimpleParticipant().isWatcher())
                .map(AllTeamDetailQueryResult::getParticipantResponse)
                .collect(Collectors.toList());
    }

    private List<WatcherResponse> toWatcherResponses(final List<AllTeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> it.getSimpleParticipant().isWatcher())
                .map(AllTeamDetailQueryResult::getWatcherResponse)
                .collect(Collectors.toList());
    }
}
