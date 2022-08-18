package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Team;
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
public class TeamDto {

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

    public static TeamDto from(final Team team, final Long hostId, final TeamStatus status,
                               final List<Long> interviewers, final List<Long> interviewees,
                               final List<ParticipantDto> participantResponses, final Boolean isParticipant) {
        return new TeamDto(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                hostId,
                status,
                isParticipant,
                interviewers,
                interviewees,
                participantResponses
        );
    }
}
