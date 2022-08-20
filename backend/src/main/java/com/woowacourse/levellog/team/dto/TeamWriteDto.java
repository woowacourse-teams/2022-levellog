package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Team;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamWriteDto {

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

    public static TeamWriteDto from(final String title, final String place, final int interviewerNumber,
                                    final LocalDateTime startAt, final List<Long> participantIds,
                                    final List<Long> watcherIds) {
        return new TeamWriteDto(title, place, interviewerNumber, startAt, participantIds, watcherIds);
    }

    public Team toEntity(final String profileUrl) {
        return new Team(title, place, startAt, profileUrl, interviewerNumber);
    }
}
