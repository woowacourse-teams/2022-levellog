package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.TeamStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamDetailResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private Long hostId;
    private TeamStatus status;
    private Boolean isParticipant;
    private List<Long> interviewers;
    private List<Long> interviewees;
    private List<ParticipantResponse> participants;
    private List<WatcherResponse> watchers;

    public static TeamDetailResponse from(final TeamResponse teamResponse, final Long hostId, final TeamStatus status,
                                          final Boolean isParticipant, final List<Long> interviewers,
                                          final List<Long> interviewees,
                                          final List<ParticipantResponse> participantResponses,
                                          final List<WatcherResponse> watcherResponses) {
        return new TeamDetailResponse(
                teamResponse.getId(),
                teamResponse.getTitle(),
                teamResponse.getPlace(),
                teamResponse.getStartAt(),
                teamResponse.getProfileUrl(),
                hostId,
                status,
                isParticipant,
                interviewers,
                interviewees,
                participantResponses,
                watcherResponses
        );
    }
}
