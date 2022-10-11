package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.query.AllTeamListDetailQueryResult;
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
public class TeamListDetailResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private TeamStatus status;
    private List<ParticipantInTeamListResponse> participants;

    public static TeamListDetailResponse of(final List<AllTeamListDetailQueryResult> simpleParticipants,
                                            final TeamStatus status) {
        final AllTeamListDetailQueryResult result = simpleParticipants.get(0);
        return new TeamListDetailResponse(
                result.getId(),
                result.getTitle(),
                result.getPlace(),
                result.getStartAt(),
                result.getTeamImage(),
                status,
                simpleParticipants.stream()
                        .map(ParticipantInTeamListResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
