package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.dto.query.AllTeamDetailListQueryResult;
import com.woowacourse.levellog.team.dto.query.AllTeamListQueryResult;
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
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    public TeamListResponse findAll(final TeamFilterCondition condition, final int page, final int size) {
        final AllTeamListQueryResult results = new AllTeamListQueryResult(
                teamQueryRepository.findAllList(condition.isClosed(), size, page * size));

        return results.toResponse(timeStandard.now());
    }

    public TeamListResponse findAllByMemberId(final Long memberId) {
        final Member member = memberRepository.getMember(memberId);
        final AllTeamListQueryResult results = new AllTeamListQueryResult(teamQueryRepository.findMyList(member));

        return results.toResponse(timeStandard.now());
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final AllTeamDetailListQueryResult results = new AllTeamDetailListQueryResult(
                teamQueryRepository.findAllByTeamId(teamId, memberId));

        validateNotEmpty(results, teamId, memberId);

        return results.toResponse(memberId, timeStandard.now());
    }

    private void validateNotEmpty(final AllTeamDetailListQueryResult allTeamDetailListQueryResult,
                                  final Long teamId, final Long memberId) {
        if (allTeamDetailListQueryResult.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("membmerId", memberId));
        }
    }
}
