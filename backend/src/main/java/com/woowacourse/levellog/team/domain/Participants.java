package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Participants {

    private final List<Participant> values;

    public List<Long> toInterviewerIds(final Long memberId, final int interviewerNumber) {
        final List<Long> participantIds = toParticipantIds();
        final int from = participantIds.indexOf(memberId) + 1;

        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear.subList(from, from + interviewerNumber);
    }

    public List<Long> toIntervieweeIds(final Long memberId, final int interviewerNumber) {
        final List<Long> participantIds = toParticipantIds();
        final int to = participantIds.indexOf(memberId) + values.size();

        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear.subList(to - interviewerNumber, to);
    }

    private List<Long> toParticipantIds() {
        return values
                .stream()
                .map(Participant::getMember)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    public boolean contains(final Long memberId) {
        return values
                .stream()
                .map(Participant::getMember)
                .map(BaseEntity::getId)
                .anyMatch(it -> it.equals(memberId));
    }

    public Long toHostId() {
        return values
                .stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow(() -> new MemberNotFoundException("모든 참가자 중 호스트가 존재하지 않습니다."))
                .getMember()
                .getId();
    }
}
