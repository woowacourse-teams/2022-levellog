package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ParticipantsFactory {

    private static final String PARTICIPANTS = "participants";
    private static final String WATCHERS = "watchers";

    private ParticipantsFactory() {
    }

    public static Participants createParticipants(final Team team, final Long hostId,
                                                  final List<Long> participantIds, final List<Long> watcherIds) {
        validate(hostId, participantIds, watcherIds, team.getInterviewerNumber());

        final List<Participant> participants = new ArrayList<>();
        participants.addAll(toParticipants(team, hostId, participantIds));
        participants.addAll(toWatchers(team, hostId, watcherIds));

        return new Participants(participants);
    }

    private static List<Participant> toParticipants(final Team team, final Long hostId,
                                                    final List<Long> participantIds) {
        return participantIds.stream()
                .map(it -> new Participant(team, it, Objects.equals(hostId, it), false))
                .collect(Collectors.toList());
    }

    private static List<Participant> toWatchers(final Team team, final Long hostId, final List<Long> watcherIds) {
        return watcherIds.stream()
                .map(it -> new Participant(team, it, Objects.equals(hostId, it), true))
                .collect(Collectors.toList());
    }

    private static void validate(final Long hostId, final List<Long> participantIds, final List<Long> watcherIds,
                                 final int interviewerNumber) {
        validateParticipantExistence(participantIds);
        validateDistinctParticipant(participantIds);
        validateDistinctWatcher(watcherIds);
        validateIndependent(participantIds, watcherIds);
        validateHostExistence(hostId, participantIds, watcherIds);
        validParticipantNumber(participantIds.size(), interviewerNumber);
    }

    private static void validateParticipantExistence(final List<Long> participantIds) {
        if (participantIds.isEmpty()) {
            throw new InvalidFieldException("참가자는 1명 이상이어야 합니다.", DebugMessage.init()
                    .append(PARTICIPANTS, participantIds));
        }
    }

    private static void validateDistinctParticipant(final List<Long> participantIds) {
        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new InvalidFieldException("중복된 참가자가 존재합니다.", DebugMessage.init()
                    .append(PARTICIPANTS, participantIds));
        }
    }

    private static void validateDistinctWatcher(final List<Long> watcherIds) {
        final Set<Long> distinct = new HashSet<>(watcherIds);
        if (distinct.size() != watcherIds.size()) {
            throw new InvalidFieldException("중복된 참관자가 존재합니다.", DebugMessage.init()
                    .append(WATCHERS, watcherIds));
        }
    }

    private static void validateIndependent(final List<Long> participantIds, final List<Long> watcherIds) {
        final boolean notIndependent = participantIds.stream()
                .anyMatch(watcherIds::contains);

        if (notIndependent) {
            throw new InvalidFieldException("참가자와 참관자에 모두 포함된 멤버가 존재합니다.", DebugMessage.init()
                    .append(PARTICIPANTS, participantIds)
                    .append(WATCHERS, watcherIds));
        }
    }

    private static void validateHostExistence(final Long hostId, final List<Long> participantIds,
                                              final List<Long> watcherIds) {
        if (!participantIds.contains(hostId) && !watcherIds.contains(hostId)) {
            throw new InvalidFieldException("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.", DebugMessage.init()
                    .append("hostId", hostId)
                    .append(PARTICIPANTS, participantIds)
                    .append(WATCHERS, watcherIds));
        }
    }

    private static void validParticipantNumber(final int participantNumber, final int interviewerNumber) {
        if (participantNumber <= interviewerNumber) {
            throw new InvalidFieldException("참가자 수는 인터뷰어 수 보다 많아야 합니다.", DebugMessage.init()
                    .append("participantNumber", participantNumber)
                    .append("interviewerNumber", interviewerNumber));
        }
    }
}
