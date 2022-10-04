package com.woowacourse.levellog.team.application;

import com.woowacourse.levellog.authentication.support.Authentic;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.SimpleParticipants;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.AllSimpleParticipantDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamListDto;
import com.woowacourse.levellog.team.dto.TeamSimpleDto;
import com.woowacourse.levellog.team.dto.WatcherDto;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamQueryService {

    private final TeamQueryRepository teamQueryRepository;
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    public TeamListDto findAll(final TeamFilterCondition condition, final int page, final int size) {
        final List<AllSimpleParticipantDto> allParticipants = teamQueryRepository.findAllList(condition.isClosed(),
                size, page * size);
        final List<TeamSimpleDto> teamSimpleDtos = allParticipants.stream()
                .collect(Collectors.groupingBy(AllSimpleParticipantDto::getId, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamSimpleDto.of(it, toTeamStatus(it.get(0))))
                .collect(Collectors.toList());

        return new TeamListDto(teamSimpleDtos);
    }

    public TeamListDto findAllByMemberId(@Authentic LoginStatus loginStatus) {
        final Member member = memberRepository.getMember(loginStatus.getMemberId());

        final List<AllSimpleParticipantDto> allParticipants = teamQueryRepository.findMyList(member);
        final List<TeamSimpleDto> teamSimpleDtos = allParticipants.stream()
                .collect(Collectors.groupingBy(AllSimpleParticipantDto::getId, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(it -> TeamSimpleDto.of(it, toTeamStatus(it.get(0))))
                .collect(Collectors.toList());

        return new TeamListDto(teamSimpleDtos);
    }

    public TeamDetailResponse findByTeamIdAndMemberId(final Long teamId, @Authentic final LoginStatus loginStatus) {
        final List<AllParticipantDto> allParticipants = teamQueryRepository.findAllByTeamId(teamId, loginStatus);
        if (allParticipants.isEmpty()) {
            throw new TeamNotFoundException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("memberId", loginStatus.getMemberId()));
        }

        final AllParticipantDto allParticipantDto = allParticipants.get(0);
        final TeamDto teamDto = allParticipantDto.getTeamDto();

        final SimpleParticipants participants = toSimpleParticipants(allParticipants);

        final TeamStatus status = toTeamStatus(allParticipantDto);
        final boolean isParticipant = participants.isContains(loginStatus.getMemberId());
        final List<Long> interviewers = participants.toInterviewerIds(loginStatus.getMemberId(),
                teamDto.getInterviewerNumber());
        final List<Long> interviewees = participants.toIntervieweeIds(loginStatus.getMemberId(),
                teamDto.getInterviewerNumber());

        return TeamDetailResponse.from(teamDto, participants.toHostId(), status, isParticipant, interviewers,
                interviewees, toParticipantDto(allParticipants), toWatcherDtos(allParticipants));
    }

    private TeamStatus toTeamStatus(final AllParticipantDto dto) {
        return TeamStatus.of(dto.isClosed(), dto.getStartAt(), timeStandard.now());
    }

    private TeamStatus toTeamStatus(final AllSimpleParticipantDto dto) {
        return TeamStatus.of(dto.isClosed(), dto.getStartAt(), timeStandard.now());
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
}
