package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.domain.TeamStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamListQueryResult {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private boolean isClosed;
    private Long memberId;
    private String nickname;
    private String memberImage;

    public TeamStatus getTeamStatus(final LocalDateTime time) {
        return TeamStatus.of(isClosed, startAt, time);
    }
}
