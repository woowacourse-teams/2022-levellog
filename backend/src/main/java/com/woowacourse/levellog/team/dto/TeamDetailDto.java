package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class TeamDetailDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private Long hostId;
    private Boolean isClosed;
    private Boolean isParticipant;
    private List<ParticipantDto> participants;

    public static TeamDetailDto from(final Team team, final Long hostId,
                                     final Boolean isParticipant, final List<ParticipantDto> participantResponses) {
        return new TeamDetailDto(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                hostId,
                team.isClosed(),
                isParticipant,
                participantResponses
        );
    }
}
