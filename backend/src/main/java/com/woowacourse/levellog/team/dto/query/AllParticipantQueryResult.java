package com.woowacourse.levellog.team.dto.query;

import com.woowacourse.levellog.team.domain.SimpleParticipant;
import com.woowacourse.levellog.team.domain.Team;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Getter;
import org.springframework.util.ReflectionUtils;

@Getter
public class AllParticipantQueryResult {

    private final Team team;
    private final Long memberId;
    private final Long levellogId;
    private final Long preQuestionId;
    private final String nickname;
    private final String profileUrl;
    private final boolean isHost;
    private final boolean isWatcher;

    public AllParticipantQueryResult(final Long id, final String title, final String place, final LocalDateTime startAt,
                                     final String teamProfileUrl, final int interviewerNumber, final boolean isClosed,
                                     final LocalDateTime createdAt, final LocalDateTime updatedAt,
                                     final Long memberId, final Long levellogId, final Long preQuestionId,
                                     final String nickname, final String profileUrl, final boolean isHost,
                                     final boolean isWatcher) {
        this.team = new Team(title, place, LocalDateTime.now().plusDays(3), teamProfileUrl, interviewerNumber);

        injectTeamFields(id, startAt, isClosed, createdAt, updatedAt);

        this.memberId = memberId;
        this.levellogId = levellogId;
        this.preQuestionId = preQuestionId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.isHost = isHost;
        this.isWatcher = isWatcher;
    }

    private void injectTeamFields(final Long id, final LocalDateTime startAt, final boolean isClosed,
                           final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        final Map<String, Object> map = Map.of("id", id, "startAt", startAt, "isClosed", isClosed,
                "createdAt", createdAt, "updatedAt", updatedAt);

        for (final Entry<String, Object> entry : map.entrySet()) {
            final Field field = ReflectionUtils.findField(Team.class, entry.getKey());
            field.setAccessible(true);
            ReflectionUtils.setField(field, team, entry.getValue());
        }
    }

    public SimpleParticipant toSimpleParticipant() {
        return new SimpleParticipant(team.getId(), memberId, isHost, isWatcher);
    }
}
