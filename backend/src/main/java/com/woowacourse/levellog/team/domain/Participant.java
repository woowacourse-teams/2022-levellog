package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.member.domain.Member;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "deleted = false")
public class Participant extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_participant_team"))
    private Team team;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_participant_member"))
    private Member member;

    private boolean isHost;

    private boolean deleted;

    public Participant(final Team team, final Member member, final boolean isHost) {
        this.team = team;
        this.member = member;
        this.isHost = isHost;
        this.deleted = false;
    }

    public void delete() {
        deleted = true;
    }
}
