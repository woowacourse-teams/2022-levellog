package com.woowacourse.levellog.teamdisplay.application;


import static org.springframework.data.domain.Sort.Direction.DESC;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import com.woowacourse.levellog.teamdisplay.dto.ParticipantDto;
import com.woowacourse.levellog.teamdisplay.dto.TeamDto;
import com.woowacourse.levellog.teamdisplay.dto.TeamListDto;
import com.woowacourse.levellog.teamdisplay.dto.WatcherDto;
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
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;
    private final PreQuestionRepository preQuestionRepository;
    private final TimeStandard timeStandard;

    public TeamListDto findAll(final Optional<String> status, final Long memberId) {
        final List<Team> teams = status.map(this::findAllByIsClosedAndOrderByCreatedAt)
                .orElseGet(this::findAllOrderByIsClosedAndCreatedAt);

        final List<TeamDto> teamDtos = toTeamDtos(memberId, teams);

        return new TeamListDto(teamDtos);
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

    public TeamDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);

        return createTeamAndRoleDto(team, memberId);
    }

    public TeamListDto findAllByMemberId(final Long memberId) {
        final List<Team> teams = getTeamsByMemberId(memberId);
        final List<TeamDto> teamDtos = toTeamDtos(memberId, teams);

        return new TeamListDto(teamDtos);
    }

    private List<TeamDto> toTeamDtos(final Long memberId, final List<Team> teams) {
        return teams.stream()
                .map(it -> createTeamAndRoleDto(it, memberId))
                .collect(Collectors.toList());
    }

    private TeamDto createTeamAndRoleDto(final Team team, final Long memberId) {
        final TeamStatus status = team.status(timeStandard.now());

        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final List<Long> interviewers = participants.toInterviewerIds(memberId, team.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, team.getInterviewerNumber());

        return TeamDto.from(team, participants.toHostId(), status, participants.isContains(memberId), interviewers,
                interviewees, toParticipantResponses(participants, memberId), toWatcherResponses(participants));
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

    private List<ParticipantDto> toParticipantResponses(final Participants participants, final Long memberId) {
        return participants.getValues().stream()
                .filter(Participant::isParticipant)
                .map(it -> createParticipantDto(it, memberId))
                .collect(Collectors.toList());
    }

    private List<WatcherDto> toWatcherResponses(final Participants participants) {
        return participants.getValues().stream()
                .filter(Participant::isWatcher)
                .map(WatcherDto::from)
                .collect(Collectors.toList());
    }

    private ParticipantDto createParticipantDto(final Participant participant, final Long memberId) {
        final Levellog levellog = getLevellog(participant);

        if (levellog == null) {
            return ParticipantDto.from(participant, null, null);
        }

        return ParticipantDto.from(participant, levellog.getId(), getPreQuestionId(memberId, levellog));
    }

    private Levellog getLevellog(final Participant participant) {
        return levellogRepository
                .findByAuthorIdAndTeamId(participant.getMember().getId(), participant.getTeam().getId())
                .orElse(null);
    }

    private Long getPreQuestionId(final Long memberId, final Levellog levellog) {
        return preQuestionRepository.findByLevellogAndAuthorId(levellog, memberId)
                .map(PreQuestion::getId)
                .orElse(null);
    }
}
