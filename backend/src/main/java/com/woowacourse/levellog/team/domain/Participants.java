package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Participants {

    private final List<Participant> values;

    public List<Long> toInterviewerIds(final Long memberId, final int interviewerNumber) {
        if (notContains(memberId)) {
            return Collections.emptyList();
        }

        final List<Long> participantIds = toParticipantIds();
        final int from = participantIds.indexOf(memberId) + 1;

        return concatSameTwice(participantIds).subList(from, from + interviewerNumber);
    }

    public List<Long> toIntervieweeIds(final Long memberId, final int interviewerNumber) {
        if (notContains(memberId)) {
            return Collections.emptyList();
        }

        final List<Long> participantIds = toParticipantIds();
        final int to = participantIds.indexOf(memberId) + values.size();

        return concatSameTwice(participantIds).subList(to - interviewerNumber, to);
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

    private boolean notContains(final Long memberId) {
        return values.stream()
                .map(Participant::getMember)
                .map(BaseEntity::getId)
                .noneMatch(it -> it.equals(memberId));
    }

    private List<Long> concatSameTwice(final List<Long> participantIds) {
        final List<Long> linear = new ArrayList<>(participantIds);
        linear.addAll(participantIds);

        return linear;
    }

    private List<Long> toParticipantIds() {
        return values.stream()
                .map(Participant::getMember)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
    }

    private void validateParticipant(final Long teamId, final Long targetMemberId, final Long memberId) {
        if (notContains(memberId)) {
            throw new UnauthorizedException("팀의 참가자만 역할을 조회할 수 있습니다. teamId : " + teamId + ", memberId : " + memberId);
        }
        if (notContains(targetMemberId)) {
            throw new ParticipantNotFoundException("memberId : " + targetMemberId + "에 해당하는 member는 "
                    + "teamId : " + teamId + "의 참가자가 아닙니다.");
        }
    }
}
