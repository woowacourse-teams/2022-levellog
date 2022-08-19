package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Participant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WatcherDto {

    private Long memberId;
    private String nickname;
    private String profileUrl;

    public static WatcherDto from(final Participant participant) {
        return new WatcherDto(
                participant.getMember().getId(),
                participant.getMember().getNickname(),
                participant.getMember().getProfileUrl());
    }
}
