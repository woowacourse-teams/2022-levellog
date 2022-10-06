package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.support.TimeStandard;
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
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final TeamWriteDto request, final Long hostId) {
        final Member host = memberRepository.getMember(hostId);
        final Team team = new Team(request.toTeamDetail(host.getProfileUrl()),
                request.toParticipantsIngredient(hostId));

        final Team savedTeam = teamRepository.save(team);

        return savedTeam.getId();
    }

    public TeamStatusDto findStatus(final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        final TeamStatus status = team.status(timeStandard.now());

        return new TeamStatusDto(status);
    }

    public InterviewRoleDto findMyRole(final Long teamId, final Long targetMemberId, final Long memberId) {
        // FIXME : 로직 수정 필요 - Team한테만 메시지 던지기
        final Team team = teamRepository.getTeamWithParticipants(teamId);
        final Participants participants = new Participants(participantRepository.findByTeam(team));
        final InterviewRole interviewRole = participants.toInterviewRole(teamId, targetMemberId, memberId,
                team.getInterviewerNumber());

        return InterviewRoleDto.from(interviewRole);
    }

    @Transactional
    public void update(final TeamWriteDto request, final Long teamId, final Long memberId) {
        final Team team = teamRepository.getTeamWithParticipants(teamId);
        team.validateHostAuthorization(memberId);

        team.update(request.toTeamDetail(team.getProfileUrl()), request.toParticipantsIngredient(memberId),
                timeStandard.now());
    }

    @Transactional
    public void close(final Long teamId, final Long memberId) {
        final Team team = teamRepository.getTeamWithParticipants(teamId);
        team.validateHostAuthorization(memberId);

        team.close(timeStandard.now());
    }

    @Transactional
    public void deleteById(final Long teamId, final Long memberId) {
        final Team team = teamRepository.getTeamWithParticipants(teamId);
        team.validateHostAuthorization(memberId);

        team.delete(timeStandard.now());
    }
}
