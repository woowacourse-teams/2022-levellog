package com.woowacourse.levellog.team.dto.request;

import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamWriteRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String place;

    @Positive
    private int interviewerNumber;

    @NotNull
    private LocalDateTime startAt;

    @Valid
    @NotEmpty
    private List<Long> participantIds;

    private List<Long> watcherIds;

    public TeamDetail toTeamDetail(final String profileUrl) {
        return new TeamDetail(title, place, startAt, profileUrl, interviewerNumber);
    }

    public Team toEntity(final Long hostId, final String profileUrl) {
        return new Team(toTeamDetail(profileUrl), hostId, participantIds, watcherIds);
    }
}
