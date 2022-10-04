package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.dto.response.TeamResponse;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AllParticipantQueryResult {

    private final Long teamId;
    private final String title;
    private final String place;
    private final LocalDateTime startAt;
    private final String profileUrl;
    private final int interviewerNumber;
    private final boolean isClosed;
    private final Long memberId;
    private final Long levellogId;
    private final Long preQuestionId;
    private final String nickname;
    private final boolean isHost;
    private final boolean isWatcher;

    public AllParticipantQueryResult(final Long id, final String title, final String place, final LocalDateTime startAt,
                                     final String profileUrl, final int interviewerNumber, final boolean isClosed,
                                     final Long memberId, final Long levellogId, final Long preQuestionId,
                                     final String nickname, final boolean isHost,
                                     final boolean isWatcher) {
        this.teamId = id;
        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.profileUrl = profileUrl;
        this.interviewerNumber = interviewerNumber;
        this.isClosed = isClosed;
        this.memberId = memberId;
        this.levellogId = levellogId;
        this.preQuestionId = preQuestionId;
        this.nickname = nickname;
        this.isHost = isHost;
        this.isWatcher = isWatcher;
    }

    public TeamResponse getTeamDto() {
        return TeamResponse.from(teamId, title, place, startAt, profileUrl, interviewerNumber, isClosed);
    }

    public SimpleParticipant toSimpleParticipant() {
        return new SimpleParticipant(teamId, memberId, isHost, isWatcher);
    }
}
