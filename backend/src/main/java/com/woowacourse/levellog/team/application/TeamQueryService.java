package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.query.AllParticipantQueryResult;
import com.woowacourse.levellog.team.dto.query.AllSimpleParticipantQueryResult;
import com.woowacourse.levellog.team.dto.query.TeamSimpleQueryResult;
import com.woowacourse.levellog.team.dto.response.ParticipantResponse;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import com.woowacourse.levellog.team.dto.response.TeamResponse;
import com.woowacourse.levellog.team.dto.response.WatcherResponse;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
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
        final List<AllSimpleParticipantQueryResult> allParticipants = teamQueryRepository.findAllList(
                condition.isClosed(),
                size, page * size);
        final List<TeamSimpleQueryResult> teamSimpleQueryResults = allParticipants.stream()
                .collect(Collectors.groupingBy(AllSimpleParticipantQueryResult::getId, LinkedHashMap::new,
                        Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamSimpleQueryResult.of(it, toTeamStatus(it.get(0))))
                .collect(Collectors.toList());

        return new TeamListResponses(teamSimpleQueryResults);
    }

    public TeamListResponses findAllByMemberId(final Long memberId) {
        final Member member = memberRepository.getMember(memberId);

        final List<AllSimpleParticipantQueryResult> allParticipants = teamQueryRepository.findMyList(member);
        final List<TeamSimpleQueryResult> teamSimpleQueryResults = allParticipants.stream()
                .collect(Collectors.groupingBy(AllSimpleParticipantQueryResult::getId, LinkedHashMap::new,
                        Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamSimpleQueryResult.of(it, toTeamStatus(it.get(0))))
                .collect(Collectors.toList());

        return new TeamListResponses(teamSimpleQueryResults);
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final List<AllParticipantQueryResult> allParticipants = teamQueryRepository.findAllByTeamId(teamId, memberId);
        if (allParticipants.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("membmerId", memberId));
        }

        final AllParticipantQueryResult allParticipantDto = allParticipants.get(0);
        final TeamResponse teamResponse = allParticipantDto.getTeamDto();

        final SimpleParticipants participants = toSimpleParticipants(allParticipants);

        final TeamStatus status = toTeamStatus(allParticipantDto);
        final boolean isParticipant = participants.isContains(memberId);
        final List<Long> interviewers = participants.toInterviewerIds(memberId, teamResponse.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, teamResponse.getInterviewerNumber());

        return TeamDetailResponse.from(teamResponse, participants.toHostId(), status, isParticipant, interviewers,
                interviewees, toParticipantDto(allParticipants), toWatcherDtos(allParticipants));
    }

    private TeamStatus toTeamStatus(final AllParticipantQueryResult result) {
        return TeamStatus.of(result.isClosed(), result.getStartAt(), timeStandard.now());
    }

    private TeamStatus toTeamStatus(final AllSimpleParticipantQueryResult result) {
        return TeamStatus.of(result.isClosed(), result.getStartAt(), timeStandard.now());
    }

    private SimpleParticipants toSimpleParticipants(final List<AllParticipantQueryResult> filteredParticipants) {
        final List<SimpleParticipant> participants = filteredParticipants.stream()
                .map(AllParticipantQueryResult::toSimpleParticipant)
                .collect(Collectors.toList());
        return new SimpleParticipants(participants);
    }

    private List<ParticipantResponse> toParticipantDto(final List<AllParticipantQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> !it.isWatcher())
                .map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    private List<WatcherResponse> toWatcherDtos(final List<AllParticipantQueryResult> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(AllParticipantQueryResult::isWatcher)
                .map(WatcherResponse::from)
                .collect(Collectors.toList());
    }
}
