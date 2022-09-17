package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamCustomRepository;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.AllSimpleParticipantDto;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamListDto;
import com.woowacourse.levellog.team.dto.TeamSimpleDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.dto.WatcherDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private static final String ALL_STATUS = "all";

    private final TeamRepository teamRepository;
    private final TeamCustomRepository teamCustomRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
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

    public TeamListDto findAll(final String status, final int page, final int size) {
        TeamStatus.checkClosed(status);

        final List<AllSimpleParticipantDto> allParticipants = teamCustomRepository.findAllSimple(size, page * size);
        final List<TeamSimpleDto> teamSimpleDtos = allParticipants.stream()
                .filter(it -> filterStatus(status, toTeamStatus(it)))
                .collect(Collectors.groupingBy(AllSimpleParticipantDto::getId, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamSimpleDto.of(it, toTeamStatus(it.get(0))))
                .collect(Collectors.toList());

        return new TeamListDto(teamSimpleDtos);
    }

    private boolean filterStatus(final String status, final TeamStatus teamStatus) {
        if (!status.equals(ALL_STATUS)) {
            return teamStatus.isSame(status);
        }
        return true;
    }

    private TeamStatus toTeamStatus(final AllSimpleParticipantDto dto) {
        return TeamStatus.of(dto.isClosed(), dto.getStartAt(), timeStandard.now());
    }

    public TeamsDto findAllByMemberId(final Long memberId) {
        final Member member = getMember(memberId);

        final List<AllParticipantDto> allParticipants = teamCustomRepository.findAllMy(member);
        final List<TeamDto> teamDtos = allParticipants.stream()
                .map(AllParticipantDto::getTeam)
                .distinct()
                .map(it -> toTeamDto(filterParticipantsByTeam(allParticipants, it), it, memberId))
                .collect(Collectors.toList());

        return new TeamsDto(teamDtos);
    }

    public TeamDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final List<AllParticipantDto> allParticipants = teamCustomRepository.findAllByTeamId(teamId, memberId);
        if (allParticipants.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("membmerId", memberId));
        }

        final Team team = allParticipants.get(0).getTeam();

        final SimpleParticipants participants = toSimpleParticipants(allParticipants);

        final TeamStatus status = team.status(timeStandard.now());
        final boolean isParticipant = participants.isContains(memberId);
        final List<Long> interviewers = participants.toInterviewerIds(memberId, team.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, team.getInterviewerNumber());

        return TeamDto.from(team, participants.toHostId(), status, isParticipant, interviewers, interviewees,
                toParticipantDto(allParticipants), toWatcherDtos(allParticipants));
    }

    private TeamDto toTeamDto(final List<AllParticipantDto> filtered, final Team team, final Long memberId) {
        final SimpleParticipants participants = toSimpleParticipants(filtered);

        final TeamStatus status = team.status(timeStandard.now());
        final boolean isParticipant = participants.isContains(memberId);
        final List<Long> interviewers = participants.toInterviewerIds(memberId, team.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, team.getInterviewerNumber());

        return TeamDto.from(team, participants.toHostId(), status, isParticipant, interviewers, interviewees,
                toParticipantDto(filtered), toWatcherDtos(filtered));
    }

    private List<AllParticipantDto> filterParticipantsByTeam(final List<AllParticipantDto> all, final Team team) {
        return all.stream()
                .filter(it -> it.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    private SimpleParticipants toSimpleParticipants(final List<AllParticipantDto> filteredParticipants) {
        final List<SimpleParticipant> participants = filteredParticipants.stream()
                .map(AllParticipantDto::toSimpleParticipant)
                .collect(Collectors.toList());
        return new SimpleParticipants(participants);
    }

    private List<ParticipantDto> toParticipantDto(final List<AllParticipantDto> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(it -> !it.isWatcher())
                .map(ParticipantDto::from)
                .collect(Collectors.toList());
    }

    private List<WatcherDto> toWatcherDtos(final List<AllParticipantDto> filteredParticipants) {
        return filteredParticipants.stream()
                .filter(AllParticipantDto::isWatcher)
                .map(WatcherDto::from)
                .collect(Collectors.toList());
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
