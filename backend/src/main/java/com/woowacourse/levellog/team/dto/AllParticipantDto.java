package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllParticipantDto {

    private final Team team;
    private final Long memberId;
    private final Long levellogId;
    private final Long preQuestionId;
    private final String nickname;
    private final String profileUrl;
    private final boolean isHost;
    private final boolean isWatcher;

    public SimpleParticipant toSimpleParticipant() {
        return new SimpleParticipant(team.getId(), memberId, isHost, isWatcher);
    }
}
