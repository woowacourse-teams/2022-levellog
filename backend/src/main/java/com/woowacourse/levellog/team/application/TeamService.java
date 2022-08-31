package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.dto.WatcherDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private static final String ALL_STATUS = "all";

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;
    private final PreQuestionRepository preQuestionRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final TeamWriteDto request, final Long hostId) {
        final Member host = getMember(hostId);
        final Team team = request.toEntity(host.getProfileUrl());
        final Participants participants = createParticipants(team, hostId, request.getParticipantIds(),
                request.getWatcherIds());
        team.validParticipantNumber(participants.size());

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants.getValues());

        return savedTeam.getId();
    }

    public TeamsDto findAll(final Pageable pageable, final String status, final Long memberId) {
        final List<Team> teams = getTeams(pageable, status);
        final List<TeamDto> teamDtos = toTeamDtos(teams, memberId);

        return new TeamsDto(teamDtos);
    }

    public TeamDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);

        return createTeamAndRoleDto(team, memberId);
    }

    public TeamsDto findAllByMemberId(final Long memberId) {
        final List<Team> teams = getTeamsByMemberId(memberId);
        final List<TeamDto> teamDtos = toTeamDtos(teams, memberId);

        return new TeamsDto(teamDtos);
    }

    public TeamStatusDto findStatus(final Long teamId) {
        final Team team = getTeam(teamId);
        final TeamStatus status = team.status(timeStandard.now());

        return new TeamStatusDto(status);
    }

    public InterviewRoleDto findMyRole(final Long teamId, final Long targetMemberId, final Long memberId) {
        final Team team = getTeam(teamId);
        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final InterviewRole interviewRole = participants.toInterviewRole(teamId, targetMemberId, memberId,
                team.getInterviewerNumber());

        return InterviewRoleDto.from(interviewRole);
    }

    @Transactional
    public void update(final TeamWriteDto request, final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHostAuthorization(memberId, team);
        team.update(request.toEntity(team.getProfileUrl()), timeStandard.now());

        final Participants participants = createParticipants(team, memberId, request.getParticipantIds(),
                request.getWatcherIds());
        team.validParticipantNumber(participants.size());
        participantRepository.deleteByTeam(team);
        participantRepository.saveAll(participants.getValues());
    }

    @Transactional
    public void close(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHostAuthorization(memberId, team);

        team.close(timeStandard.now());
    }

    @Transactional
    public void deleteById(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHostAuthorization(memberId, team);

        participantRepository.deleteByTeam(team);
        team.delete(timeStandard.now());
    }

    private List<Team> getTeams(final Pageable pageable, final String status) {
        if (status.equals(ALL_STATUS)) {
            return findAllOrderByIsClosedAndCreatedAt(pageable);
        }
        return findAllByIsClosedAndOrderByCreatedAt(pageable, status);
    }

    private List<Team> findAllOrderByIsClosedAndCreatedAt(final Pageable pageable) {
        return teamRepository.findAll(pageable)
                .getContent();
    }

    private List<Team> findAllByIsClosedAndOrderByCreatedAt(final Pageable pageable, final String status) {
        final List<Team> teams = teamRepository.findAllByIsClosed(TeamStatus.checkClosed(status), pageable)
                .getContent();

        return filteringTeamByStatus(status, teams);
    }

    private List<Team> filteringTeamByStatus(final String status, final List<Team> teams) {
        return teams.stream()
                .filter(it -> it.isSameStatus(status, timeStandard.now()))
                .collect(Collectors.toList());
    }

    private List<TeamDto> toTeamDtos(final List<Team> teams, final Long memberId) {
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

    private Participants createParticipants(final Team team, final Long hostId, final List<Long> participantIds,
                                            final List<Long> watcherIds) {
        validateParticipantExistence(participantIds);
        validateDistinctParticipant(participantIds);
        validateDistinctWatcher(watcherIds);
        validateIndependent(participantIds, watcherIds);
        validateHostExistence(hostId, participantIds, watcherIds);

        final List<Participant> participants = new ArrayList<>();
        participants.addAll(toParticipants(team, hostId, participantIds));
        participants.addAll(toWatchers(team, hostId, watcherIds));

        return new Participants(participants);
    }

    private void validateParticipantExistence(final List<Long> participantsIds) {
        if (participantsIds.isEmpty()) {
            throw new InvalidFieldException("참가자가 존재하지 않습니다.", DebugMessage.init()
                    .append("participants", participantsIds));
        }
    }

    private void validateDistinctParticipant(final List<Long> participantIds) {
        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new InvalidFieldException("중복된 참가자가 존재합니다.", DebugMessage.init()
                    .append("participants", participantIds));
        }
    }

    private void validateDistinctWatcher(final List<Long> watcherIds) {
        final Set<Long> distinct = new HashSet<>(watcherIds);
        if (distinct.size() != watcherIds.size()) {
            throw new InvalidFieldException("중복된 참관자가 존재합니다.", DebugMessage.init()
                    .append("watchers", watcherIds));
        }
    }

    private void validateHostExistence(final Long hostId, final List<Long> participantIds,
                                       final List<Long> watcherIds) {
        if (!participantIds.contains(hostId) && !watcherIds.contains(hostId)) {
            throw new InvalidFieldException("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.", DebugMessage.init()
                    .append("hostId", hostId)
                    .append("participants", participantIds)
                    .append("watchers", watcherIds));
        }
    }

    private void validateIndependent(final List<Long> participantIds, final List<Long> watcherIds) {
        final boolean notIndependent = participantIds.stream()
                .anyMatch(watcherIds::contains);

        if (notIndependent) {
            throw new InvalidFieldException("참가자와 참관자에 모두 포함된 멤버가 존재합니다.", DebugMessage.init()
                    .append("particiapnts", participantIds)
                    .append("watchers", watcherIds));
        }
    }

    private List<Participant> toParticipants(final Team team, final Long hostId, final List<Long> participantIds) {
        return participantIds.stream()
                .map(it -> new Participant(team, getMember(it), it.equals(hostId), false))
                .collect(Collectors.toList());
    }

    private List<Participant> toWatchers(final Team team, final Long hostId, final List<Long> watcherIds) {
        return watcherIds.stream()
                .map(it -> new Participant(team, getMember(it), it.equals(hostId), true))
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

    private void validateHostAuthorization(final Long memberId, final Team team) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final Long hostId = participants.toHostId();

        if (!memberId.equals(participants.toHostId())) {
            throw new HostUnauthorizedException(DebugMessage.init()
                    .append("hostId", hostId)
                    .append("memberId", memberId));
        }
    }
}
