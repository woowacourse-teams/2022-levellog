package com.woowacourse.levellog.team.application;

<<<<<<< HEAD
import com.woowacourse.levellog.common.exception.InvalidFieldException;
=======
>>>>>>> main
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
<<<<<<< HEAD
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.TeamAndRoleDto;
import com.woowacourse.levellog.team.dto.TeamAndRolesDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.exception.DuplicateParticipantsException;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
=======
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.dto.ParticipantResponse;
import com.woowacourse.levellog.team.dto.TeamRequest;
import com.woowacourse.levellog.team.dto.TeamResponse;
import com.woowacourse.levellog.team.dto.TeamUpdateRequest;
import com.woowacourse.levellog.team.dto.TeamsResponse;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.util.ArrayList;
import java.util.List;
>>>>>>> main
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
<<<<<<< HEAD
@Transactional(readOnly = true)
=======
@Transactional
>>>>>>> main
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;
<<<<<<< HEAD
    private final PreQuestionRepository preQuestionRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final TeamCreateDto request, final Long hostId) {
        final Member host = getMember(hostId);
        final Team team = request.toEntity(host.getProfileUrl());
        final Participants participants = getParticipants(team, hostId, request.getParticipants().getIds());
        team.validParticipantNumber(participants.size());

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants.getValues());
=======

    public Long save(final Long hostId, final TeamRequest request) {
        final Member host = getMember(hostId);
        final Team team = new Team(request.getTitle(), request.getPlace(), request.getStartAt(), host.getProfileUrl());
        final List<Participant> participants = getParticipants(hostId, request.getParticipants().getIds(), team);

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants);
>>>>>>> main

        return savedTeam.getId();
    }

<<<<<<< HEAD
    public TeamAndRolesDto findAll(final Long memberId) {
        final List<Team> teams = teamRepository.findAll();
        final List<TeamAndRoleDto> teamAndRoles = teams.stream()
                .map(it -> findByTeamIdAndMemberId(it.getId(), memberId))
                .collect(Collectors.toList());

        return new TeamAndRolesDto(teamAndRoles);
    }

    public TeamsDto findAllByMemberId(final Long memberId) {
        final List<Team> teams = getTeamsByMemberId(memberId);

        return new TeamsDto(getTeamResponses(teams, memberId));
    }

    public TeamAndRoleDto findByTeamIdAndMemberId(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        final List<Long> interviewers = participants.toInterviewerIds(memberId, team.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(memberId, team.getInterviewerNumber());

        return TeamAndRoleDto.from(team, participants.toHostId(), interviewers, interviewees,
                getParticipantResponses(participants, memberId), participants.isContains(memberId));
    }

    public InterviewRoleDto findMyRole(final Long teamId, final Long targetMemberId, final Long memberId) {
        final Team team = getTeam(teamId);
        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final InterviewRole interviewRole = participants.toInterviewRole(teamId, targetMemberId, memberId,
                team.getInterviewerNumber());

        return InterviewRoleDto.from(interviewRole);
    }

    @Transactional
    public void update(final TeamUpdateDto request, final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        validateHost(memberId, team);
=======
    public TeamsResponse findAll() {
        final List<Team> teams = teamRepository.findAll();
        return new TeamsResponse(getTeamResponses(teams));
    }

    public TeamResponse findById(final Long id) {
        final Team team = getTeam(id);
        return getTeamResponse(team);
    }

    public void update(final Long id, final TeamUpdateRequest request, final Long memberId) {
        final Team team = getTeam(id);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException();
        }
>>>>>>> main

        team.update(request.getTitle(), request.getPlace(), request.getStartAt());
    }

<<<<<<< HEAD
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
        teamRepository.deleteById(teamId);
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

    private Participants getParticipants(final Team team, final Long hostId, final List<Long> memberIds) {
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

    private List<TeamDto> getTeamResponses(final List<Team> teams, final Long memberId) {
        return teams.stream()
                .map(it -> getTeamResponse(it, memberId))
                .collect(Collectors.toList());
    }

    private TeamDto getTeamResponse(final Team team, final Long memberId) {
        final Participants participants = new Participants(participantRepository.findByTeam(team));

        return TeamDto.from(team, participants.toHostId(), participants.isContains(memberId),
                getParticipantResponses(participants, memberId));
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
=======
    public void deleteById(final Long id, final Long memberId) {
        final Team team = getTeam(id);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException();
        }

        participantRepository.deleteByTeam(team);
        teamRepository.deleteById(id);
    }

    private Member getMember(final Long hostId) {
        return memberRepository.findById(hostId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Team getTeam(final Long id) {
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }

    private List<Participant> getParticipants(final Long hostId, final List<Long> memberIds, final Team team) {
        final List<Participant> participants = new ArrayList<>();
        addMemberToParticipants(hostId, participants, team, true);
        for (final Long memberId : memberIds) {
            addMemberToParticipants(memberId, participants, team, false);
        }

        return participants;
    }

    private void addMemberToParticipants(final Long memberId, final List<Participant> participants, final Team team,
                                         final boolean isHost) {
        final Member member = getMember(memberId);
        participants.add(new Participant(team, member, isHost));
    }

    private List<TeamResponse> getTeamResponses(final List<Team> teams) {
        return teams.stream()
                .map(this::getTeamResponse)
                .collect(Collectors.toList());
    }

    private TeamResponse getTeamResponse(final Team team) {
        final List<Participant> participants = participantRepository.findByTeam(team);
        return new TeamResponse(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                getHostId(participants),
                getParticipantResponses(participants));
    }

    private Long getHostId(final List<Participant> participants) {
        return participants.stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow(MemberNotFoundException::new)
                .getMember()
                .getId();
    }

    private List<ParticipantResponse> getParticipantResponses(final List<Participant> participants) {
        return participants.stream()
                .map(it -> new ParticipantResponse(
                        it.getMember().getId(),
                        getLevellogId(it),
                        it.getMember().getNickname(),
                        it.getMember().getProfileUrl()))
                .collect(Collectors.toList());
    }

    private Long getLevellogId(final Participant participant) {
        final Levellog levellog = levellogRepository
                .findByAuthorIdAndTeamId(participant.getMember().getId(), participant.getTeam().getId())
                .orElse(null);

        if (levellog == null) {
            return null;
        }
        return levellog.getId();
>>>>>>> main
    }
}
