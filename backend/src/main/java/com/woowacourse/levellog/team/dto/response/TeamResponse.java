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
@AllArgsConstructor
public class TeamResponse {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String profileUrl;
    private int interviewerNumber;
    private boolean isClosed;
}
