package com.woowacourse.levellog.admin.dto;

import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
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
public class AdminTeamDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private TeamStatus status;

    public static AdminTeamDto of(final Team team, final TeamStatus status) {
        final TeamDetail detail = team.getDetail();
        return new AdminTeamDto(team.getId(), detail.getTitle(), detail.getPlace(), detail.getStartAt(), status);
    }
}
