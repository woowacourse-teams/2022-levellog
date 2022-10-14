package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.query.TeamListQueryResult;
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
public class TeamListResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private TeamStatus status;
    private List<ParticipantInTeamListResponse> participants;

    public static TeamListResponse of(final List<TeamListQueryResult> simpleParticipants,
                                      final TeamStatus status) {
        final TeamListQueryResult result = simpleParticipants.get(0);
        return new TeamListResponse(
                result.getId(),
                result.getTitle(),
                result.getPlace(),
                result.getStartAt(),
                result.getTeamImage(),
                status,
                simpleParticipants.stream()
                        .map(it -> new ParticipantInTeamListResponse(it.getMemberId(), it.getNickname(),
                                it.getMemberImage()))
                        .collect(Collectors.toList())
        );
    }
}
