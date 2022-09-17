package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.TeamStatus;
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
public class TeamSimpleDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private TeamStatus status;
    private List<ParticipantProfileUrlDto> participants;

    public static TeamSimpleDto of(final List<AllSimpleParticipantDto> simpleParticipants, final TeamStatus status) {
        final AllSimpleParticipantDto dto = simpleParticipants.get(0);
        return new TeamSimpleDto(
                dto.getId(),
                dto.getTitle(),
                dto.getPlace(),
                dto.getStartAt(),
                dto.getTeamImage(),
                status,
                simpleParticipants.stream()
                        .map(AllSimpleParticipantDto::getMemberImage)
                        .map(ParticipantProfileUrlDto::new)
                        .collect(Collectors.toList())
        );
    }

    @Getter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    public static class ParticipantProfileUrlDto {

        private String profileUrl;
    }
}
