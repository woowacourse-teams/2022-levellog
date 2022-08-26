package com.woowacourse.levellog.teamdisplay.application;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import com.woowacourse.levellog.teamdisplay.domain.ParticipantDetail;
import com.woowacourse.levellog.teamdisplay.domain.TeamDisplay;
import com.woowacourse.levellog.teamdisplay.domain.TeamDisplayRepository;
import com.woowacourse.levellog.teamdisplay.dto.TeamDetailDto;
import com.woowacourse.levellog.teamdisplay.dto.TeamListDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamDisplayService {

    private final TeamRepository teamRepository;
    private final TeamDisplayRepository teamDisplayRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    public TeamListDto findAll(final Optional<String> status, final Long memberId) {
        final List<TeamDisplay> items = status.map(this::findAllByIsClosedAndOrderByCreatedAt)
                .orElseGet(this::findAllOrderByIsClosedAndCreatedAt)
                .stream()
                .map(this::getTeamDisplay)
                .collect(Collectors.toList());

        return TeamListDto.from(items, memberId);
    }

    private List<Team> findAllByIsClosedAndOrderByCreatedAt(final String status) {
        final List<Team> teams = teamRepository.findAllByIsClosed(
                TeamStatus.checkClosed(status),
                Sort.by(DESC, "createdAt")
        );

        return filteringTeamByStatus(status, teams);
    }

    private List<Team> filteringTeamByStatus(final String status, final List<Team> teams) {
        return teams.stream()
                .filter(it -> it.isSameStatus(status, timeStandard.now()))
                .collect(Collectors.toList());
    }

    private List<Team> findAllOrderByIsClosedAndCreatedAt() {
        final Sort sort = Sort.by(
                Sort.Order.asc("isClosed"),
                Sort.Order.desc("createdAt")
        );

        return teamRepository.findAll(sort);
    }

    public TeamDetailDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        Member member = null;
        if (memberId != -1) {
            member = getMember(memberId);
        }

        final TeamDisplay teamDisplay = getTeamDisplay(team);
        final List<ParticipantDetail> participantDetails = teamDisplayRepository.findAllParticipantDetail(member, team);

        return TeamDetailDto.of(teamDisplay, participantDetails, memberId);
    }

    public TeamListDto findAllByMemberId(final Long memberId) {
        final List<TeamDisplay> items = getTeamsByMemberId(memberId)
                .stream()
                .map(this::getTeamDisplay)
                .collect(Collectors.toList());

        return TeamListDto.from(items, memberId);
    }

    private TeamDisplay getTeamDisplay(final Team team) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        return new TeamDisplay(team, team.status(timeStandard.now()), participants);
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(DebugMessage.init()
                        .append("memberId", memberId)));
    }

    private Team getTeam(final Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new TeamNotFoundException(DebugMessage.init()
                                .append("teamId", teamId)));
    }

    private List<Team> getTeamsByMemberId(final Long memberId) {
        final Member member = getMember(memberId);
        final List<Participant> participants = participantRepository.findAllByMember(member);

        return participants.stream()
                .map(Participant::getTeam)
                .collect(Collectors.toList());
    }
}
