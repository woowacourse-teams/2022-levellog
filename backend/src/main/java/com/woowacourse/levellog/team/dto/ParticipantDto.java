package com.woowacourse.levellog.team.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipantDto {

    private Long memberId;
    private Long levellogId;
    private Long preQuestionId;
    private String nickname;
    private String profileUrl;

    public static ParticipantDto from(final AllParticipantDto allParticipantDto) {
        return new ParticipantDto(
                allParticipantDto.getMemberId(),
                allParticipantDto.getLevellogId(),
                allParticipantDto.getPreQuestionId(),
                allParticipantDto.getNickname(),
                allParticipantDto.getProfileUrl());
    }
}
