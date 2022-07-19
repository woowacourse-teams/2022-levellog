package com.woowacourse.levellog.application;

import com.woowacourse.levellog.authentication.exception.MemberNotFoundException;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Participant;
import com.woowacourse.levellog.domain.ParticipantRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.TeamRequest;
import com.woowacourse.levellog.dto.TeamsResponse;
import java.util.ArrayList;
import java.util.List;
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
        for (final Long memberId : memberIds) {
            final Member member = getMember(memberId);
            participants.add(new Participant(team, member, hostId.equals(memberId)));
        }
        return participants;
    }
}
