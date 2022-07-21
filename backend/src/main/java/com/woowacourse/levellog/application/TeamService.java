package com.woowacourse.levellog.application;

import com.woowacourse.levellog.authentication.exception.MemberNotFoundException;
import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Participant;
import com.woowacourse.levellog.domain.ParticipantRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.ParticipantResponse;
import com.woowacourse.levellog.dto.TeamRequest;
import com.woowacourse.levellog.dto.TeamResponse;
import com.woowacourse.levellog.dto.TeamUpdateRequest;
import com.woowacourse.levellog.dto.TeamsResponse;
import com.woowacourse.levellog.exception.HostUnauthorizedException;
import com.woowacourse.levellog.exception.TeamNotFoundException;
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

    private Member getMember(final Long hostId) {
        return memberRepository.findById(hostId)
                .orElseThrow(MemberNotFoundException::new);
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

    public TeamsResponse findAll() {
        final List<Team> teams = teamRepository.findAll();
        return new TeamsResponse(getTeamResponses(teams));
    }

    public TeamResponse findById(final Long id) {
        final Team team = findTeam(id);
        return getTeam(team);
    }

    private List<TeamResponse> getTeamResponses(final List<Team> teams) {
        return teams.stream()
                .map(this::getTeam)
                .collect(Collectors.toList());
    }

    private TeamResponse getTeam(final Team team) {
        final List<Participant> participants = participantRepository.findByTeam(team);
        return new TeamResponse(
                team.getId(),
                team.getTitle(),
                team.getPlace(),
                team.getStartAt(),
                team.getProfileUrl(),
                getHostId(participants),
                getParticipantsResponse(participants));
    }

    private Long getHostId(final List<Participant> participants) {
        return participants.stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow(MemberNotFoundException::new)
                .getMember()
                .getId();
    }

    private List<ParticipantResponse> getParticipantsResponse(final List<Participant> participants) {
        return participants.stream()
                .map(it -> new ParticipantResponse(
                        it.getId(),
                        getLevellog(it),
                        it.getMember().getNickname(),
                        it.getMember().getProfileUrl()))
                .collect(Collectors.toList());
    }

    private Long getLevellog(final Participant participant) {
        final Levellog levellog = levellogRepository
                .findByAuthorIdAndTeamId(participant.getMember().getId(), participant.getTeam().getId())
                .orElse(null);

        if (levellog == null) {
            return null;
        }
        return levellog.getId();
    }

    public void update(final Long id, final TeamUpdateRequest request, final Long memberId) {
        final Team team = findTeam(id);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException();
        }

        team.update(request.getTitle(), request.getPlace(), request.getStartAt());
    }

    public void deleteById(final Long id, final Long memberId) {
        final Team team = findTeam(id);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException();
        }

        participantRepository.deleteByTeam(team);
        teamRepository.deleteById(id);
    }

    private Team findTeam(final Long id) {
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }
}
