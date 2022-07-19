package com.woowacourse.levellog.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamRequest {

    private String title;
    private String place;
    private LocalDateTime startAt;
    private ParticipantIdsRequest participants;
}
