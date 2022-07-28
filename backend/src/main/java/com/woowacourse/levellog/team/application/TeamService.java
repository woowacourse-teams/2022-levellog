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
import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Long save(final TeamCreateDto request, final Long hostId) {
        final Member host = getMember(hostId);
        final Team team = request.toEntity(host.getProfileUrl());
        final List<Participant> participants = getParticipants(team, hostId, request.getParticipants().getIds());

        final Team savedTeam = teamRepository.save(team);
        participantRepository.saveAll(participants);

        return savedTeam.getId();
    }

    public TeamsDto findAll() {
        return new TeamsDto(getTeamResponses(teamRepository.findAll()));
    }

    public TeamDto findById(final Long teamId) {
        return getTeamResponse(getTeam(teamId));
    }

    @Transactional
    public void update(final TeamUpdateDto request, final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException("호스트 권한이 없습니다. 입력한 memberId : [" + memberId + "]");
        }

        team.update(request.getTitle(), request.getPlace(), request.getStartAt());
    }

    @Transactional
    public void deleteById(final Long teamId, final Long memberId) {
        final Team team = getTeam(teamId);
        final List<Participant> participants = participantRepository.findByTeam(team);
        final Long hostId = getHostId(participants);

        if (!memberId.equals(hostId)) {
            throw new HostUnauthorizedException("호스트 권한이 없습니다. 입력한 memberId : [" + memberId + "]");
        }

        participantRepository.deleteByTeam(team);
        teamRepository.deleteById(teamId);
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private Team getTeam(final Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다. 입력한 팀 id : [" + teamId + "]"));
    }

    private List<Participant> getParticipants(final Team team, final Long hostId, final List<Long> memberIds) {
        final List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(team, getMember(hostId), true));
        participants.addAll(toParticipants(team, memberIds));

        return participants;
    }

    private List<Participant> toParticipants(final Team team, final List<Long> memberIds) {
        return memberIds.stream()
                .map(it -> new Participant(team, getMember(it), false))
                .collect(Collectors.toList());
    }

    private List<TeamDto> getTeamResponses(final List<Team> teams) {
        return teams.stream()
                .map(this::getTeamResponse)
                .collect(Collectors.toList());
    }

    private TeamDto getTeamResponse(final Team team) {
        final List<Participant> participants = participantRepository.findByTeam(team);
        return TeamDto.from(team, getHostId(participants), getParticipantResponses(participants));
    }

    private Long getHostId(final List<Participant> participants) {
        return participants.stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow(() -> new MemberNotFoundException("호스트 존재 안함"))
                .getMember()
                .getId();
    }

    private List<ParticipantDto> getParticipantResponses(final List<Participant> participants) {
        return participants.stream()
                .map(it -> ParticipantDto.from(it, getLevellogId(it)))
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
