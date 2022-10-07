package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ParticipantsIngredient {

    private final Long hostId;
    private final List<Long> participantIds;
    private final List<Long> watcherIds;

    public ParticipantsIngredient(final Long hostId, final List<Long> participantIds, final List<Long> watcherIds) {
        this.hostId = hostId;
        this.participantIds = participantIds;
        this.watcherIds = watcherIds;
    }

    public List<Participant> toParticipants(final Team team) {
        return participantIds.stream()
                .map(it -> new Participant(team, it, Objects.equals(hostId, it), false))
                .collect(Collectors.toList());
    }

    public List<Participant> toWatchers(final Team team) {
        return watcherIds.stream()
                .map(it -> new Participant(team, it, Objects.equals(hostId, it), true))
                .collect(Collectors.toList());
    }

    public void validate(final int interviewerNumber) {
        validateParticipantExistence();
        validateDistinctParticipant();
        validateDistinctWatcher();
        validateIndependent();
        validateHostExistence();
        validParticipantNumber(participantIds.size(), interviewerNumber);
    }

    private void validateParticipantExistence() {
        if (participantIds.isEmpty()) {
            throw new InvalidFieldException("참가자가 존재하지 않습니다.", DebugMessage.init()
                    .append("participants", participantIds));
        }
    }

    private void validateDistinctParticipant() {
        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new InvalidFieldException("중복된 참가자가 존재합니다.", DebugMessage.init()
                    .append("participants", participantIds));
        }
    }

    private void validateDistinctWatcher() {
        final Set<Long> distinct = new HashSet<>(watcherIds);
        if (distinct.size() != watcherIds.size()) {
            throw new InvalidFieldException("중복된 참관자가 존재합니다.", DebugMessage.init()
                    .append("watchers", watcherIds));
        }
    }

    private void validateIndependent() {
        final boolean notIndependent = participantIds.stream()
                .anyMatch(watcherIds::contains);

        if (notIndependent) {
            throw new InvalidFieldException("참가자와 참관자에 모두 포함된 멤버가 존재합니다.", DebugMessage.init()
                    .append("participants", participantIds)
                    .append("watchers", watcherIds));
        }
    }

    private void validateHostExistence() {
        if (!participantIds.contains(hostId) && !watcherIds.contains(hostId)) {
            throw new InvalidFieldException("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.", DebugMessage.init()
                    .append("hostId", hostId)
                    .append("participants", participantIds)
                    .append("watchers", watcherIds));
        }
    }

    private void validParticipantNumber(final int participantNumber, final int interviewerNumber) {
        if (participantNumber <= interviewerNumber) {
            throw new InvalidFieldException("참가자 수는 인터뷰어 수 보다 많아야 합니다.", DebugMessage.init()
                    .append("participantNumber", participantNumber)
                    .append("interviewerNumber", interviewerNumber));
        }
    }
}
