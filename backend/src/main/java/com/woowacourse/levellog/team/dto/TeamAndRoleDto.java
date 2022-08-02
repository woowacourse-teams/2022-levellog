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
public class TeamAndRoleDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private Long hostId;
    private List<Long> interviewers;
    private List<Long> interviewees;
    private List<ParticipantDto> participants;

    public static TeamAndRoleDto from(final Team team, final Long hostId, final List<Long> interviewers,
                                      final List<Long> interviewees, final List<ParticipantDto> participantResponses) {
        return new TeamAndRoleDto(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                hostId,
                interviewers,
                interviewees,
                participantResponses
        );
    }
}
