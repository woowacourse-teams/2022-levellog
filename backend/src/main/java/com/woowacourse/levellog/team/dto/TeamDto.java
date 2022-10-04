package com.woowacourse.levellog.team.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamDto {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String profileUrl;
    private int interviewerNumber;
    private boolean isClosed;

    public static TeamDto from(final Long id, final String title, final String place, final LocalDateTime startAt,
                               final String profileUrl, final int interviewerNumber, final boolean isClosed) {
        return new TeamDto(id, title, place, startAt, profileUrl, interviewerNumber, isClosed);
    }
}
