package com.woowacourse.levellog.team.dto.response;

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
public class TeamResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String profileUrl;
    private int interviewerNumber;
    private boolean isClosed;

    public static TeamResponse from(final Long id, final String title, final String place, final LocalDateTime startAt,
                                    final String profileUrl, final int interviewerNumber, final boolean isClosed) {
        return new TeamResponse(id, title, place, startAt, profileUrl, interviewerNumber, isClosed);
    }
}
