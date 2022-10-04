package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.dto.query.TeamSimpleQueryResult;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamListDto {

    private List<TeamSimpleQueryResult> teams;
}
