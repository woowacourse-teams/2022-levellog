package com.woowacourse.levellog.team.dto;

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
    private List<ParticipantDto> participants;
    private List<WatcherDto> watchers;

    public static TeamDetailResponse from(final TeamDto teamDto, final Long hostId, final TeamStatus status,
                                          final Boolean isParticipant, final List<Long> interviewers,
                                          final List<Long> interviewees,
                                          final List<ParticipantDto> participantResponses,
                                          final List<WatcherDto> watcherResponses) {
        return new TeamDetailResponse(
                teamDto.getId(),
                teamDto.getTitle(),
                teamDto.getPlace(),
                teamDto.getStartAt(),
                teamDto.getProfileUrl(),
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
