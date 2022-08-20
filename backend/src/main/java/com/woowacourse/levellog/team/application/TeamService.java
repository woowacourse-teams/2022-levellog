package com.woowacourse.levellog.team.application;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

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

    public TeamsDto findAll(final Optional<String> status, final Long memberId) {
        final List<Team> teams = status.map(this::findAllByIsClosedAndOrderByCreatedAt)
                .orElseGet(this::findAllOrderByIsClosedAndCreatedAt);

        final List<TeamDto> teamDtos = toTeamDtos(memberId, teams);

        return new TeamsDto(teamDtos);
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

    public TeamsDto findAllByMemberId(final Long memberId) {
        final List<Team> teams = getTeamsByMemberId(memberId);
        final List<TeamDto> teamDtos = toTeamDtos(memberId, teams);

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
                interviewees, getParticipantResponses(participants, memberId), getWatcherResponses(participants));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private Team getTeam(final Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [" + teamId + "]", "팀이 존재하지 않습니다."));
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
        validateHostExistence(hostId, participantIds, watcherIds);

        final List<Participant> participants = new ArrayList<>();
        participants.addAll(toParticipants(team, hostId, participantIds));
        participants.addAll(toWatchers(team, hostId, watcherIds));

        validateIndependent(participantIds, watcherIds);

        return new Participants(participants);
    }

    private void validateParticipantExistence(final List<Long> participantIds) {
        if (participantIds.isEmpty()) {
            throw new InvalidFieldException("참가자가 존재하지 않습니다.");
        }
    }

    private void validateDistinctParticipant(final List<Long> participantIds) {
        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new InvalidFieldException("중복된 참가자가 존재합니다.");
        }
    }

    private void validateDistinctWatcher(final List<Long> watcherIds) {
        final Set<Long> distinct = new HashSet<>(watcherIds);
        if (distinct.size() != watcherIds.size()) {
            throw new InvalidFieldException("중복된 참관자가 존재합니다.");
        }
    }

    private void validateHostExistence(final Long hostId, final List<Long> participantIds,
                                       final List<Long> watcherIds) {
        if (!participantIds.contains(hostId) && !watcherIds.contains(hostId)) {
            throw new InvalidFieldException("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.");
        }
    }

    private void validateIndependent(final List<Long> participantIds, final List<Long> watcherIds) {
        final boolean notIndependent = participantIds.stream()
                .anyMatch(watcherIds::contains);

        if (notIndependent) {
            throw new InvalidFieldException("참관자와 참관자 모두 참여할 수 없습니다.");
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

    private List<ParticipantDto> getParticipantResponses(final Participants participants, final Long memberId) {
        return participants.getValues().stream()
                .filter(Participant::isParticipant)
                .map(it -> createParticipantDto(it, memberId))
                .collect(Collectors.toList());
    }

    private List<WatcherDto> getWatcherResponses(final Participants participants) {
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
            throw new HostUnauthorizedException("호스트 권한이 없습니다. [hostId : " + hostId + ", memberId : " + memberId + "]");
        }
    }
}
