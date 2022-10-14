package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.request.TeamWriteRequest;
import com.woowacourse.levellog.team.dto.response.InterviewRoleResponse;
import com.woowacourse.levellog.team.dto.response.TeamStatusResponse;
import com.woowacourse.levellog.team.support.TimeStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final TeamWriteRequest request, @Verified final LoginStatus loginStatus) {
        final Long hostId = loginStatus.getMemberId();
        final Member host = memberRepository.getMember(hostId);
        final Team team = request.toEntity(hostId, host.getProfileUrl());

        final Team savedTeam = teamRepository.save(team);

        return savedTeam.getId();
    }

    public TeamStatusResponse findStatus(final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        final TeamStatus status = team.status(timeStandard.now());

        return new TeamStatusResponse(status);
    }

    public InterviewRoleResponse findMyRole(final Long teamId, final Long targetMemberId,
                                            @Verified final LoginStatus loginStatus) {
        final Team team = teamRepository.getTeam(teamId);
        final InterviewRole interviewRole = team.matchInterviewRole(targetMemberId, loginStatus.getMemberId());

        return new InterviewRoleResponse(interviewRole);
    }

    @Transactional
    public void update(final TeamWriteRequest request, final Long teamId, @Verified final LoginStatus loginStatus) {
        final Team team = teamRepository.getTeam(teamId);
        final Long memberId = loginStatus.getMemberId();
        team.validateHostAuthorization(memberId);

        team.update(request.toTeamDetail(team.getProfileUrl()), memberId, request.getParticipantIds(),
                request.getWatcherIds(), timeStandard.now());
    }

    @Transactional
    public void close(final Long teamId, @Verified final LoginStatus loginStatus) {
        final Team team = teamRepository.getTeam(teamId);
        team.validateHostAuthorization(loginStatus.getMemberId());

        team.close(timeStandard.now());
    }

    @Transactional
    public void delete(final Long teamId, @Verified final LoginStatus loginStatus) {
        final Team team = teamRepository.getTeam(teamId);
        team.validateHostAuthorization(loginStatus.getMemberId());

        team.delete(timeStandard.now());
    }
}
