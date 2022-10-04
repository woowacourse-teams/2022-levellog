package com.woowacourse.levellog.admin.dto.response;

import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class AdminTeamResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private TeamStatus status;

    public static AdminTeamResponse of(final Team team, final TeamStatus status) {
        return new AdminTeamResponse(team.getId(), team.getTitle(), team.getPlace(), team.getStartAt(), status);
    }
}
