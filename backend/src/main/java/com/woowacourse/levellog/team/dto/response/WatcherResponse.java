package com.woowacourse.levellog.team.dto.response;

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
public class WatcherResponse {

    private Long memberId;
    private String nickname;
    private String profileUrl;

    public static WatcherResponse from(final Participant participant) {
        final Member member = participant.getMember();

        return new WatcherResponse(
                member.getId(),
                member.getNickname(),
                member.getProfileUrl());
    }

    public static WatcherResponse from(final AllParticipantQueryResult allParticipantQueryResult) {
        return new WatcherResponse(
                allParticipantQueryResult.getMemberId(),
                allParticipantQueryResult.getNickname(),
                allParticipantQueryResult.getProfileUrl());
    }
}
