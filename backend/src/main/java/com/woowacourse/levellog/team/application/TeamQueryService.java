package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.dto.query.TeamDetailQueryResults;
import com.woowacourse.levellog.team.dto.query.TeamListQueryResults;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final TeamQueryRepository teamQueryRepository;
    private final TimeStandard timeStandard;

    public TeamListResponses findAll(final TeamFilterCondition condition, final int page, final int size) {
        final TeamListQueryResults results = new TeamListQueryResults(
                teamQueryRepository.findAllList(condition.isClosed(), size, page * size));

        return results.toResponse(timeStandard.now());
    }

    public TeamListResponses findAllByMemberId(@Verified final LoginStatus loginStatus) {
        final TeamListQueryResults results = new TeamListQueryResults(
                teamQueryRepository.findMyList(loginStatus.getMemberId()));

        return results.toResponse(timeStandard.now());
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, final LoginStatus loginStatus) {
        final TeamDetailQueryResults results = new TeamDetailQueryResults(
                teamQueryRepository.findAllByTeamId(teamId, loginStatus));

        validateNotEmpty(results, teamId, loginStatus);

        return results.toResponse(loginStatus.getMemberId(), timeStandard.now());
    }

    private void validateNotEmpty(final TeamDetailQueryResults teamDetailQueryResults,
                                  final Long teamId, final LoginStatus loginStatus) {
        if (teamDetailQueryResults.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("memberId", loginStatus.getMemberId()));
        }
    }
}
