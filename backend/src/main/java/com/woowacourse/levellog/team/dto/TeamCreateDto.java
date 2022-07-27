package com.woowacourse.levellog.team.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String place;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private ParticipantIdsDto participants;
}
