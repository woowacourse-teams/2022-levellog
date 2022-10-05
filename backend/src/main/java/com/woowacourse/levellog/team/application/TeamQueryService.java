package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.dto.query.AllTeamDetailQueryResults;
import com.woowacourse.levellog.team.dto.query.AllTeamListQueryResult;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.List;
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

    public TeamListResponses findAll(final TeamFilterCondition condition, final int page, final int size) {
        final List<AllTeamListQueryResult> results = teamQueryRepository.findAllList(
                condition.isClosed(), size, page * size);

        return TeamListResponses.from(results, timeStandard.now());
    }

    public TeamListResponses findAllByMemberId(final Long memberId) {
        final Member member = memberRepository.getMember(memberId);
        final List<AllTeamListQueryResult> results = teamQueryRepository.findMyList(member);

        return TeamListResponses.from(results, timeStandard.now());
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final AllTeamDetailQueryResults results = new AllTeamDetailQueryResults(
                teamQueryRepository.findAllByTeamId(teamId, memberId));

        validateNotEmpty(results, teamId, memberId);

        return results.toResponses(memberId, timeStandard.now());
    }

    private void validateNotEmpty(final AllTeamDetailQueryResults allTeamDetailQueryResults,
                                  final Long teamId, final Long memberId) {
        if (allTeamDetailQueryResults.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("membmerId", memberId));
        }
    }
}
