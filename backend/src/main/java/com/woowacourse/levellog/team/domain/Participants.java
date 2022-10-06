package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
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

    public static Participants of(final Team team, final ParticipantsIngredient ingredient) {
        ingredient.validate(team.getInterviewerNumber());
        final List<Participant> participants = new ArrayList<>();

        participants.addAll(ingredient.toParticipants(team));
        participants.addAll(ingredient.toWatchers(team));

        return new Participants(participants);
    }

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

    private Long toHostId() {
        return values
                .stream()
                .filter(Participant::isHost)
                .findAny()
                .orElseThrow()
                .getMemberId();
    }

    public void validateHost(final Long memberId){
        final Long hostId = toHostId();
        if (!hostId.equals(memberId)) {
            throw new HostUnauthorizedException(DebugMessage.init()
                    .append("hostId", toHostId())
                    .append("memberId", memberId));
        }
    }

    public InterviewRole toInterviewRole(final Long teamId, final Long targetMemberId, final Long memberId,
                                         final int interviewerNumber) {
        validateParticipant(teamId, targetMemberId, memberId);
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

    public int size() {
        return values.size();
    }

    private boolean isContains(final Long memberId) {
        return values.stream()
                .map(Participant::getMemberId)
                .anyMatch(it -> it.equals(memberId));
    }

    private boolean isWatcher(final Long memberId) {
        return values.stream()
                .filter(it -> it.getMemberId().equals(memberId))
                .anyMatch(Participant::isWatcher);
    }

    private List<Long> concatSameTwice(final List<Long> participantIds) {
        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear;
    }

    private List<Long> toParticipantIds() {
        return values.stream()
                .filter(Participant::isParticipant)
                .map(Participant::getMemberId)
                .collect(Collectors.toList());
    }

    private void validateParticipant(final Long teamId, final Long targetMemberId, final Long memberId) {
        if (!isContains(memberId)) {
            throw new ParticipantNotSameTeamException(DebugMessage.init()
                    .append("memberId", memberId)
                    .append("teamId", teamId));
        }

        if (!isContains(targetMemberId)) {
            throw new ParticipantNotFoundException(DebugMessage.init()
                    .append("memberId", targetMemberId)
                    .append("teamId", teamId));
        }
    }

    public void update(final Participants target) {
        clear();
        values.addAll(target.values);
    }

    public void clear() {
        values.clear();
    }
}
