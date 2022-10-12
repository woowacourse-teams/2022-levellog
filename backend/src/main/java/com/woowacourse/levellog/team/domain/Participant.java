package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Participant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_participant_team"))
    private Team team;

    private Long memberId;

    private boolean isHost;

    private boolean isWatcher;

    public Participant(final Team team, final Long memberId, final boolean isHost, final boolean isWatcher) {
        this.team = team;
        this.memberId = memberId;
        this.isHost = isHost;
        this.isWatcher = isWatcher;
    }

    public boolean isParticipant() {
        return !isWatcher;
    }

    public boolean isSameMemberId(final Long memberId) {
        return this.memberId.equals(memberId);
    }
}
