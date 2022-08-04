package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Participant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ParticipantDto {

    private Long memberId;
    private Long levellogId;
    private Long preQuestionId;
    private String nickname;
    private String profileUrl;

    public static ParticipantDto from(final Participant participant, final Long levellogId, final Long preQuestionId) {
        return new ParticipantDto(
                participant.getMember().getId(),
                levellogId,
                preQuestionId,
                participant.getMember().getNickname(),
                participant.getMember().getProfileUrl());
    }
}
