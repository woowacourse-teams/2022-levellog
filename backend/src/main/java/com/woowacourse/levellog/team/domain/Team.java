package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE team SET deleted = true WHERE id=?")
public class Team extends BaseEntity {

    @Embedded
    private TeamDetail detail;

    @Column(nullable = false)
    private boolean isClosed;

    @Column(nullable = false)
    private boolean deleted;

    @Embedded
    private Participants participants = new Participants();

    public Team(final TeamDetail teamDetail, final ParticipantsIngredient ingredient) {
        this.detail = teamDetail;
        this.participants = Participants.of(this, ingredient);
        this.isClosed = false;
        this.deleted = false;
    }

    public void update(final TeamDetail detail, final ParticipantsIngredient ingredient,
                       final LocalDateTime presentTime) {
        validateReady(presentTime);
        this.detail = detail;
        updateParticipants(ingredient);
    }

    public void close(final LocalDateTime presentTime) {
        validateInProgress(presentTime);
        isClosed = true;
    }

    public void validateInProgress(final LocalDateTime presentTime) {
        validateAlreadyClosed();
        if (!detail.isStarted(presentTime)) {
            throw new TeamNotInProgressException(DebugMessage.init()
                    .append("presentTime", presentTime));
        }
    }

    private void validateAlreadyClosed() {
        if (isClosed) {
            throw new TeamAlreadyClosedException(DebugMessage.init()
                    .append("teamId", getId()));
        }
    }

    public void delete(final LocalDateTime presentTime) {
        validateReady(presentTime);

        this.participants.clear();
        this.deleted = true;
    }

    public void validateReady(final LocalDateTime presentTime) {
        if (detail.isStarted(presentTime)) {
            throw new TeamNotReadyException(DebugMessage.init()
                    .append("presentTime", presentTime));
        }
    }

    public TeamStatus status(final LocalDateTime presentTime) {
        if (isClosed) {
            return TeamStatus.CLOSED;
        }
        if (!detail.isStarted(presentTime)) {
            return TeamStatus.READY;
        }
        return TeamStatus.IN_PROGRESS;
    }

    public void updateParticipants(final ParticipantsIngredient ingredient) {
        final Participants target = Participants.of(this, ingredient);

        participants.update(target);
    }

    public void validateHostAuthorization(final Long memberId) {
        participants.validateHost(memberId);
    }

    public void validateIsParticipants(final Long memberId) {
        participants.validateIsParticipants(this.getId(), memberId);
    }

    public InterviewRole getInterviewRole(final Long targetMemberId, final Long sourceMemberId) {
        return participants.toInterviewRole(getId(), targetMemberId, sourceMemberId, getInterviewerNumber());
    }

    public int getInterviewerNumber() {
        return detail.getInterviewerNumber();
    }

    public String getProfileUrl() {
        return detail.getProfileUrl();
    }
}
