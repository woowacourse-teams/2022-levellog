package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.TeamStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TeamStatusResponse {

    private TeamStatus status;
}
