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
public class ParticipantSimpleDto {

    private Long memberId;
    private String profileUrl;

    public static ParticipantSimpleDto from(final AllSimpleParticipantDto allSimpleParticipantDto) {
        return new ParticipantSimpleDto(
                allSimpleParticipantDto.getMemberId(),
                allSimpleParticipantDto.getMemberImage()
        );
    }
}
