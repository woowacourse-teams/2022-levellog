package com.woowacourse.levellog.team.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamListDto {

    private List<TeamSimpleDto> teams;
}
