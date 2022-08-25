package com.woowacourse.levellog.teamdisplay.dto;

import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.teamdisplay.domain.TeamDisplay;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamListDto {

    private List<TeamListItem> teams;

    public static TeamListDto from(final List<TeamDisplay> teamDisplays, final Long memberId) {
        final List<TeamListItem> items = teamDisplays.stream()
                .map(teamDisplay -> TeamListItem.of(teamDisplay, memberId))
                .collect(Collectors.toList());

        return new TeamListDto(items);
    }

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TeamListItem {

        private Long id;
        private String title;
        private String place;
        private LocalDateTime startAt;
        private String teamImage;
        private Long hostId;
        private TeamStatus status;
        private Boolean isParticipant;
        private List<MemberDto> participants;
        private List<MemberDto> watchers;

        public static TeamListItem of(final TeamDisplay teamDisplay, final Long memberId) {
            final Team team = teamDisplay.getTeam();
            final TeamStatus status = teamDisplay.getStatus();
            final Participants units = teamDisplay.getUnits();
            final List<MemberDto> participants = toParticipants(units);
            final List<MemberDto> watchers = toWatchers(units);

            return new TeamListItem(
                    team.getId(),
                    team.getTitle(),
                    team.getPlace(),
                    team.getStartAt(),
                    team.getProfileUrl(),
                    units.toHostId(),
                    status,
                    units.isContains(memberId),
                    participants,
                    watchers
            );
        }

        private static List<MemberDto> toParticipants(final Participants units) {
            return units.getValues()
                    .stream()
                    .filter(Participant::isParticipant)
                    .map(Participant::getMember)
                    .map(MemberDto::from)
                    .collect(Collectors.toList());
        }

        private static List<MemberDto> toWatchers(final Participants units) {
            return units.getValues()
                    .stream()
                    .filter(Participant::isWatcher)
                    .map(Participant::getMember)
                    .map(MemberDto::from)
                    .collect(Collectors.toList());
        }
    }
}
