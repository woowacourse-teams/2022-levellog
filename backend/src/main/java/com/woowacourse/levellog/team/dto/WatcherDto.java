package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.member.domain.Member;
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
public class WatcherDto {

    private Long memberId;
    private String nickname;
    private String profileUrl;

    public static WatcherDto from(final Participant participant) {
        final Member member = participant.getMember();

        return new WatcherDto(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl());
    }

    public static WatcherDto from(final AllParticipantQueryResult allParticipantQueryResult) {
        return new WatcherDto(
                allParticipantQueryResult.getMemberId(),
                allParticipantQueryResult.getNickname(),
                allParticipantQueryResult.getProfileUrl());
    }
}
