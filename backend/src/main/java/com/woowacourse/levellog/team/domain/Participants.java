package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Participants {

    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Participant> values = new ArrayList<>();

    public void update(final Participants target) {
        clear();
        values.addAll(target.values);
    }

    public void clear() {
        values.clear();
    }

    public void validateHost(final Long memberId) {
        final Long hostId = toHostId();
        if (!hostId.equals(memberId)) {
            throw new HostUnauthorizedException(DebugMessage.init()
                    .append("hostId", toHostId())
                    .append("memberId", memberId));
        }
    }

    public void validateIsParticipants(final Long teamId, final Long memberId) {
        if (!isContains(memberId)) {
            throw new NotParticipantException(DebugMessage.init()
                    .append("teamId", teamId)
                    .append("memberId", memberId));
        }
    }

    public InterviewRole toInterviewRole(final Long targetMemberId, final Long memberId, final int interviewerNumber) {
        if (targetMemberId.equals(memberId)) {
            return InterviewRole.ME;
        }

        final boolean isInterviewer = toInterviewerIds(targetMemberId, interviewerNumber)
                .stream()
                .anyMatch(it -> it.equals(memberId));
        if (isInterviewer) {
            return InterviewRole.INTERVIEWER;
        }

        return InterviewRole.OBSERVER;
    }

    private Long toHostId() {
        return values.stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow()
                .getMemberId();
    }

    private List<Long> toInterviewerIds(final Long memberId, final int interviewerNumber) {
        if (!isContains(memberId) || isWatcher(memberId)) {
            return Collections.emptyList();
        }

        final List<Long> participantIds = toParticipantIds();
        final int from = participantIds.indexOf(memberId) + 1;

        return concatSameTwice(participantIds).subList(from, from + interviewerNumber);
    }

    private List<Long> concatSameTwice(final List<Long> participantIds) {
        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear;
    }

    private boolean isContains(final Long memberId) {
        return values.stream()
                .anyMatch(it -> it.isSameMemberId(memberId));
    }

    private boolean isWatcher(final Long memberId) {
        return values.stream()
                .filter(it -> it.isSameMemberId(memberId))
                .anyMatch(Participant::isWatcher);
    }

    private List<Long> toParticipantIds() {
        return values.stream()
                .filter(Participant::isParticipant)
                .map(Participant::getMemberId)
                .collect(Collectors.toList());
    }
}
