package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.response.ParticipantResponse;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamResponse;
import com.woowacourse.levellog.team.dto.response.WatcherResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AllTeamDetailListQueryResult {

    private final List<AllTeamDetailQueryResult> results;

    public AllTeamDetailListQueryResult(final List<AllTeamDetailQueryResult> results) {
        this.results = results;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public TeamDetailResponse toResponse(final Long memberId, final LocalDateTime time) {
        final AllTeamDetailQueryResult result = results.get(0);

        final TeamResponse teamResponse = result.getTeamResponse();
        final TeamStatus status = toTeamStatus(result, time);
        final SimpleParticipants participants = toSimpleParticipants();

        return TeamDetailResponse.from(
                teamResponse,
                status,
                memberId,
                participants,
                toParticipantResponses(results),
                toWatcherResponses(results)
        );
    }

    private SimpleParticipants toSimpleParticipants() {
        return results.stream()
                .map(AllTeamDetailQueryResult::getSimpleParticipant)
                .collect(Collectors.collectingAndThen(Collectors.toList(), SimpleParticipants::new));
    }

    private TeamStatus toTeamStatus(final AllTeamDetailQueryResult result, final LocalDateTime nowTime) {
        return result.getTeamStatus(nowTime);
    }

    private List<ParticipantResponse> toParticipantResponses(
            final List<AllTeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> !it.isWatcher())
                .map(AllTeamDetailQueryResult::getParticipantResponse)
                .collect(Collectors.toList());
    }

    private List<WatcherResponse> toWatcherResponses(final List<AllTeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(AllTeamDetailQueryResult::isWatcher)
                .map(AllTeamDetailQueryResult::getWatcherResponse)
                .collect(Collectors.toList());
    }
}
