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
import com.woowacourse.levellog.team.dto.TeamAndRoleDto;
import com.woowacourse.levellog.team.dto.TeamAndRolesDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.exception.DuplicateParticipantsException;
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
        final Participants participants = createParticipants(team, hostId, request.getParticipants().getIds());
        team.validParticipantNumber(participants.size());

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants.getValues());

        return savedTeam.getId();
    }

    public TeamAndRolesDto findAll(final Optional<String> status, final Long memberId) {
        final List<Team> teams;
        teams = status.map(this::findAllByIsClosedAndOrderByCreatedAt)
                .orElseGet(this::findAllOrderByIsClosedAndCreatedAt);

        final List<TeamAndRoleDto> teamAndRoles = teams.stream()
                .map(it -> createTeamAndRoleDto(it, memberId))
                .collect(Collectors.toList());

        return new TeamAndRolesDto(teamAndRoles);
    }

    private List<Team> findAllByIsClosedAndOrderByCreatedAt(final String status) {
        final List<Team> teams = teamRepository.findAllByIsClosed(
                TeamStatus.isClosed(status),
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

    public TeamAndRoleDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);

        return createTeamAndRoleDto(team, memberId);
    }

    public TeamAndRolesDto findAllByMemberId(final Long memberId) {
        final List<Team> teams = getTeamsByMemberId(memberId);

        final List<TeamAndRoleDto> teamAndRoles = teams.stream()
                .map(it -> createTeamAndRoleDto(it, memberId))
                .collect(Collectors.toList());

        return new TeamAndRolesDto(teamAndRoles);
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
        validateHost(memberId, team);
        team.update(request.toEntity(team.getProfileUrl()), timeStandard.now());

        final Participants participants = createParticipants(team, memberId, request.getParticipants().getIds());
        team.validParticipantNumber(participants.size());
        participantRepository.deleteByTeam(team);
        participantRepository.saveAll(participants.getValues());
    }

    @Transactional
    public void close(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHost(memberId, team);

        team.close(timeStandard.now());
    }

    @Transactional
    public void deleteById(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHost(memberId, team);

        participantRepository.deleteByTeam(team);
        team.delete(timeStandard.now());
    }

    private TeamAndRoleDto createTeamAndRoleDto(final Team team, final Long memberId) {
        final TeamStatus status = team.status(timeStandard.now());

        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final List<Long> interviewers = participants.toInterviewerIds(memberId, team.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, team.getInterviewerNumber());

        return TeamAndRoleDto.from(team, participants.toHostId(), status, interviewers, interviewees,
                getParticipantResponses(participants, memberId), participants.isContains(memberId));
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

    private Participants createParticipants(final Team team, final Long hostId, final List<Long> memberIds) {
        validateOtherParticipantExistence(memberIds);
        validateParticipantDuplication(memberIds, hostId);

        final List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(team, getMember(hostId), true));
        participants.addAll(toParticipants(team, memberIds));

        return new Participants(participants);
    }

    private void validateOtherParticipantExistence(final List<Long> memberIds) {
        if (memberIds.isEmpty()) {
            throw new InvalidFieldException("호스트 이외의 참가자가 존재하지 않습니다.");
        }
    }

    private void validateParticipantDuplication(final List<Long> memberIds, final Long hostId) {
        final List<Long> participantIds = new ArrayList<>(memberIds);
        participantIds.add(hostId);

        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new DuplicateParticipantsException(
                    "참가자 중복 [participants : " + participantIds + " hostId : " + hostId + "]");
        }
    }

    private List<Participant> toParticipants(final Team team, final List<Long> memberIds) {
        return memberIds.stream()
                .map(it -> new Participant(team, getMember(it), false))
                .collect(Collectors.toList());
    }

    private List<ParticipantDto> getParticipantResponses(final Participants participants, final Long memberId) {
        return participants.getValues().stream()
                .map(it -> createParticipantDto(it, memberId))
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

    private void validateHost(final Long memberId, final Team team) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final Long hostId = participants.toHostId();

        if (!memberId.equals(participants.toHostId())) {
            throw new HostUnauthorizedException("호스트 권한이 없습니다. [hostId : " + hostId + ", memberId : " + memberId + "]");
        }
    }
}
