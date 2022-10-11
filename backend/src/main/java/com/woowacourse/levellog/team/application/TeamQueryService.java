package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.dto.query.TeamDetailListQueryResult;
import com.woowacourse.levellog.team.dto.query.TeamListQueryResult;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponse;
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

    public TeamListResponse findAll(final TeamFilterCondition condition, final int page, final int size) {
        final TeamListQueryResult results = new TeamListQueryResult(
                teamQueryRepository.findAllList(condition.isClosed(), size, page * size));

        return results.toResponse(timeStandard.now());
    }

    public TeamListResponse findAllByMemberId(@Verified final LoginStatus loginStatus) {
        final TeamListQueryResult results = new TeamListQueryResult(
                teamQueryRepository.findMyList(loginStatus.getMemberId()));

        return results.toResponse(timeStandard.now());
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, final LoginStatus loginStatus) {
        final TeamDetailListQueryResult results = new TeamDetailListQueryResult(
                teamQueryRepository.findAllByTeamId(teamId, loginStatus));

        validateNotEmpty(results, teamId, loginStatus);

        return results.toResponse(loginStatus.getMemberId(), timeStandard.now());
    }

    private void validateNotEmpty(final TeamDetailListQueryResult teamDetailListQueryResult,
                                  final Long teamId, final LoginStatus loginStatus) {
        if (teamDetailListQueryResult.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("memberId", loginStatus.getMemberId()));
        }
    }
}
