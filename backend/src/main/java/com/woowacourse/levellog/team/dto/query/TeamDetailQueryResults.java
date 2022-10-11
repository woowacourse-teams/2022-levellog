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

public class TeamDetailQueryResults {

    private final List<TeamDetailQueryResult> results;

    public TeamDetailQueryResults(final List<TeamDetailQueryResult> results) {
        this.results = results;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public TeamDetailResponse toResponse(final Long memberId, final LocalDateTime time) {
        final TeamDetailQueryResult result = results.get(0);

        final TeamResponse teamResponse = result.getTeamResponse();
        final TeamStatus status = result.getTeamStatus(time);
        final SimpleParticipants participants = toSimpleParticipants();

        return new TeamDetailResponse(
                teamResponse.getId(),
                teamResponse.getTitle(),
                teamResponse.getPlace(),
                teamResponse.getStartAt(),
                teamResponse.getProfileUrl(),
                status,
                participants.toHostId(),
                participants.isContains(memberId),
                participants.toInterviewerIds(memberId, teamResponse.getInterviewerNumber()),
                participants.toIntervieweeIds(memberId, teamResponse.getInterviewerNumber()),
                toParticipantResponses(results),
                toWatcherResponses(results)
        );
    }

    private SimpleParticipants toSimpleParticipants() {
        return results.stream()
                .map(TeamDetailQueryResult::getSimpleParticipant)
                .collect(Collectors.collectingAndThen(Collectors.toList(), SimpleParticipants::new));
    }

    private List<ParticipantResponse> toParticipantResponses(
            final List<TeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> !it.isWatcher())
                .map(TeamDetailQueryResult::getParticipantResponse)
                .collect(Collectors.toList());
    }

    private List<WatcherResponse> toWatcherResponses(final List<TeamDetailQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(TeamDetailQueryResult::isWatcher)
                .map(TeamDetailQueryResult::getWatcherResponse)
                .collect(Collectors.toList());
    }
}
