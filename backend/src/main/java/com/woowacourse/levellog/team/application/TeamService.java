package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final LevellogRepository levellogRepository;

    public Long save(final Long hostId, final TeamRequest request) {
        final Member host = getMember(hostId);
        final Team team = new Team(request.getTitle(), request.getPlace(), request.getStartAt(), host.getProfileUrl());
        final List<Participant> participants = getParticipants(hostId, request.getParticipants().getIds(), team);

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants);

        return savedTeam.getId();
    }

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

        team.update(request.getTitle(), request.getPlace(), request.getStartAt());
    }

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
    }
}
