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
    private final String teamProfileUrl;
    private final int interviewerNumber;
    private final boolean isClosed;
    private final Long memberId;
    private final Long levellogId;
    private final Long preQuestionId;
    private final String nickname;
    private final String profileUrl;
    private final boolean isHost;
    private final boolean isWatcher;

    public AllParticipantQueryResult(final Long id, final String title, final String place, final LocalDateTime startAt,
                                     final String teamProfileUrl, final int interviewerNumber, final boolean isClosed,
                                     final Long memberId, final Long levellogId, final Long preQuestionId,
                                     final String nickname, final String profileUrl,
                                     final boolean isHost, final boolean isWatcher) {
        this.teamId = id;
        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.teamProfileUrl = teamProfileUrl;
        this.interviewerNumber = interviewerNumber;
        this.isClosed = isClosed;
        this.memberId = memberId;
        this.levellogId = levellogId;
        this.preQuestionId = preQuestionId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.isHost = isHost;
        this.isWatcher = isWatcher;
    }

    public TeamResponse getTeamResponse() {
        return TeamResponse.from(teamId, title, place, startAt, profileUrl, interviewerNumber, isClosed);
    }

    public SimpleParticipant toSimpleParticipant() {
        return new SimpleParticipant(teamId, memberId, isHost, isWatcher);
    }
}
