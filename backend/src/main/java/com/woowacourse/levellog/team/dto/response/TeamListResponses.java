package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.dto.query.TeamSimpleQueryResult;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamListResponses {

    private List<TeamSimpleQueryResult> teams;
}
