package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import java.time.LocalDateTime;
import java.util.List;
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

    public Team(final TeamDetail teamDetail, final Long hostId,
                final List<Long> participantIds, final List<Long> watcherIds) {
        this.detail = teamDetail;
        this.isClosed = false;
        this.deleted = false;
        this.participants = ParticipantsFactory.createParticipants(this, hostId, participantIds, watcherIds);
    }

    public void update(final TeamDetail detail, final Long hostId, final List<Long> participantsIds,
                       final List<Long> watcherIds, final LocalDateTime presentTime) {
        validateReady(presentTime);

        this.detail = detail;
        participants.update(ParticipantsFactory.createParticipants(this, hostId, participantsIds, watcherIds));
    }

    public void delete(final LocalDateTime presentTime) {
        validateReady(presentTime);

        participants.clear();
        deleted = true;
    }

    public void close(final LocalDateTime presentTime) {
        validateInProgress(presentTime);
        isClosed = true;
    }

    public TeamStatus status(final LocalDateTime presentTime) {
        return TeamStatus.of(isClosed, detail.getStartAt(), presentTime);
    }

    public void validateReady(final LocalDateTime presentTime) {
        status(presentTime).validateReady();
    }

    public void validateInProgress(final LocalDateTime presentTime) {
        status(presentTime).validateInProgress();
    }

    public void validateHostAuthorization(final Long memberId) {
        participants.validateHost(memberId);
    }

    public void validateIsParticipants(final Long memberId) {
        participants.validateIsParticipants(this.getId(), memberId);
    }

    public InterviewRole matchInterviewRole(final Long targetMemberId, final Long sourceMemberId) {
        validateIsParticipants(targetMemberId);
        validateIsParticipants(sourceMemberId);

        return participants.toInterviewRole(targetMemberId, sourceMemberId, getInterviewerNumber());
    }

    public int getInterviewerNumber() {
        return detail.getInterviewerNumber();
    }

    public String getProfileUrl() {
        return detail.getProfileUrl();
    }

}
