package com.woowacourse.levellog.teamdisplay.dto;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.teamdisplay.domain.ParticipantDetail;
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
public class TeamDetailDto {

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

    public static TeamDetailDto of(final TeamDisplay teamDisplay, final List<ParticipantDetail> participantDetails,
                                   final Long memberId) {
        final Team team = teamDisplay.getTeam();
        final TeamStatus status = teamDisplay.getStatus();
        final Participants units = teamDisplay.getUnits();
        final List<ParticipantDto> participants = toParticipants(participantDetails);
        final List<WatcherDto> watchers = toWatchers(units);

        return new TeamDetailDto(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                units.toHostId(),
                status,
                units.isContains(memberId),
                units.toInterviewerIds(memberId, team.getInterviewerNumber()),
                units.toIntervieweeIds(memberId, team.getInterviewerNumber()),
                participants,
                watchers
        );

    }

    private static List<ParticipantDto> toParticipants(final List<ParticipantDetail> participantDetails) {
        return participantDetails.stream()
                .map(ParticipantDto::of)
                .collect(Collectors.toList());
    }

    private static List<WatcherDto> toWatchers(final Participants units) {
        return units.getValues()
                .stream()
                .filter(Participant::isWatcher)
                .map(WatcherDto::from)
                .collect(Collectors.toList());
    }

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ParticipantDto {

        private Long memberId;
        private Long levellogId;
        private Long preQuestionId;
        private String nickname;
        private String profileUrl;

        public static ParticipantDto of(final ParticipantDetail participantDetail) {
            return new ParticipantDto(
                    participantDetail.getMemberId(),
                    participantDetail.getLevellogId(),
                    participantDetail.getPreQuestionId(),
                    participantDetail.getNickname(),
                    participantDetail.getProfileUrl()
            );
        }
    }

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class WatcherDto {

        private Long memberId;
        private String nickname;
        private String profileUrl;

        public static WatcherDto from(final Participant participant) {
            final Member member = participant.getMember();

            return new WatcherDto(
                    member.getId(),
                    member.getNickname(),
                    member.getProfileUrl());
        }
    }
}
