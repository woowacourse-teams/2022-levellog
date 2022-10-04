package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.dto.query.AllParticipantQueryResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipantResponse {

    private Long memberId;
    private Long levellogId;
    private Long preQuestionId;
    private String nickname;
    private String profileUrl;

    public static ParticipantResponse from(final Participant participant, final Long levellogId,
                                           final Long preQuestionId) {
        return new ParticipantResponse(
                participant.getMember().getId(),
                levellogId,
                preQuestionId,
                participant.getMember().getNickname(),
                participant.getMember().getProfileUrl());
    }

    public static ParticipantResponse from(final AllParticipantQueryResult allParticipantQueryResult) {
        return new ParticipantResponse(
                allParticipantQueryResult.getMemberId(),
                allParticipantQueryResult.getLevellogId(),
                allParticipantQueryResult.getPreQuestionId(),
                allParticipantQueryResult.getNickname(),
                allParticipantQueryResult.getProfileUrl());
    }
}
