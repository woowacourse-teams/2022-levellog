package com.woowacourse.levellog.team.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantIdsDto {

    @NotEmpty
    private List<Long> ids;
}
