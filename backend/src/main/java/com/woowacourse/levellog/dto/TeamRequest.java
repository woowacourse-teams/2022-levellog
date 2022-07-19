package com.woowacourse.levellog.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String place;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private ParticipantIdsRequest participants;
}
