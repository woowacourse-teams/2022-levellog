package com.woowacourse.levellog.team.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SimpleParticipants {

    private final List<SimpleParticipant> values;

    public List<Long> toInterviewerIds(final Long memberId, final int interviewerNumber) {
        if (!isContains(memberId) || isWatcher(memberId)) {
            return Collections.emptyList();
        }

        final List<Long> participantIds = toParticipantIds();
        final int from = participantIds.indexOf(memberId) + 1;

        return concatSameTwice(participantIds).subList(from, from + interviewerNumber);
    }

    public List<Long> toIntervieweeIds(final Long memberId, final int interviewerNumber) {
        if (!isContains(memberId) || isWatcher(memberId)) {
            return Collections.emptyList();
        }

        final List<Long> participantIds = toParticipantIds();
        final int to = participantIds.indexOf(memberId) + participantIds.size();

        return concatSameTwice(participantIds).subList(to - interviewerNumber, to);
    }

    public Long toHostId() {
        return values
                .stream()
                .filter(SimpleParticipant::isHost)
                .findAny()
                .orElseThrow()
                .getMemberId();
    }

    public boolean isContains(final Long memberId) {
        return values.stream()
                .map(SimpleParticipant::getMemberId)
                .anyMatch(it -> it.equals(memberId));
    }

    private boolean isWatcher(final Long memberId) {
        return values.stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .anyMatch(SimpleParticipant::isWatcher);
    }

    private List<Long> concatSameTwice(final List<Long> participantIds) {
        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear;
    }

    private List<Long> toParticipantIds() {
        return values.stream()
                .filter(SimpleParticipant::isParticipant)
                .map(SimpleParticipant::getMemberId)
                .collect(Collectors.toList());
    }
}
