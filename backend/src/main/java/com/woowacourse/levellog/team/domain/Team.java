package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private static final int DEFAULT_STRING_SIZE = 255;
    private static final int PROFILE_URL_SIZE = 2048;
    private static final int MIN_INTERVIEWER_NUMBER = 1;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false, length = PROFILE_URL_SIZE)
    private String profileUrl;

    @Column(nullable = false)
    private int interviewerNumber;

    @Column(nullable = false)
    private boolean isClosed;

    @Column(nullable = false)
    private boolean deleted;

    @Embedded
    private Participants participants = new Participants();

    public Team(final String title, final String place, final LocalDateTime startAt, final String profileUrl,
                final int interviewerNumber) {
        validate(title, place, startAt, profileUrl, interviewerNumber);

        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.profileUrl = profileUrl;
        this.interviewerNumber = interviewerNumber;
        this.isClosed = false;
        this.deleted = false;
    }
//
//    public Team(final String title, final String place, final LocalDateTime startAt, final String profileUrl,
//                final int interviewerNumber, final Participants participants) {
//        validate(title, place, startAt, profileUrl, interviewerNumber);
//
//        this.title = title;
//        this.place = place;
//        this.startAt = startAt;
//        this.profileUrl = profileUrl;
//        this.interviewerNumber = interviewerNumber;
//        this.isClosed = false;
//        this.deleted = false;
//        this.participants = participants;
//    }

    private void validate(final String title, final String place, final LocalDateTime startAt, final String profileUrl,
                          final int interviewerNumber) {
        validateTitle(title);
        validatePlace(place);
        validateStartAt(startAt);
        validateProfileUrl(profileUrl);
        validateInterviewerNumber(interviewerNumber);
    }

    private void validateTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidFieldException("팀 이름이 null 또는 공백입니다.", DebugMessage.init()
                    .append("title", title));
        }
        if (title.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("팀 이름은 " + DEFAULT_STRING_SIZE + " 이하여야 합니다.", DebugMessage.init()
                    .append("title 길이", title.length()));
        }
    }

    private void validatePlace(final String place) {
        if (place == null || place.isBlank()) {
            throw new InvalidFieldException("장소가 null 또는 공백입니다.", DebugMessage.init()
                    .append("place", place));
        }
        if (place.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("장소 이름은 " + DEFAULT_STRING_SIZE + " 이하여야 합니다.", DebugMessage.init()
                    .append("place 길이", place.length()));
        }
    }

    private void validateStartAt(final LocalDateTime startAt) {
        if (startAt == null) {
            throw new InvalidFieldException("시작 시간이 없습니다.", DebugMessage.init()
                    .append("startAt", startAt));
        }

        final LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(startAt)) {
            throw new InvalidFieldException("인터뷰 시작 시간은 현재 시간 이후여야 합니다.", DebugMessage.init()
                    .append("startAt", startAt)
                    .append("now", now));
        }
    }

    private void validateProfileUrl(final String profileUrl) {
        if (profileUrl == null || profileUrl.isBlank()) {
            throw new InvalidFieldException("팀 프로필 사진이 null 또는 공백입니다.", DebugMessage.init()
                    .append("profileUrl", profileUrl));
        }
        if (profileUrl.length() > PROFILE_URL_SIZE) {
            throw new InvalidFieldException("잘못된 팀 프로필 사진을 입력했습니다.", DebugMessage.init()
                    .append("profileUrl", profileUrl));
        }
    }

    private void validateInterviewerNumber(final int interviewerNumber) {
        if (interviewerNumber < MIN_INTERVIEWER_NUMBER) {
            throw new InvalidFieldException("팀 생성시 인터뷰어 수는 " + MIN_INTERVIEWER_NUMBER + "명 이상이어야 합니다.",
                    DebugMessage.init()
                            .append("interviewerNumber", interviewerNumber));
        }
    }

    public void update(final Team team, final LocalDateTime presentTime) {
        validateReady(presentTime);

        this.title = team.title;
        this.place = team.place;
        this.startAt = team.startAt;
        this.profileUrl = team.profileUrl;
        this.interviewerNumber = team.interviewerNumber;
    }

    public void update(final Team team, final Long hostId, final List<Long> participantIds, final List<Long> watcherIds,
                       final LocalDateTime presentTime) {
        validateReady(presentTime);

        this.title = team.title;
        this.place = team.place;
        this.startAt = team.startAt;
        this.profileUrl = team.profileUrl;
        this.interviewerNumber = team.interviewerNumber;

        this.participants.clear();
        updateParticipants(hostId, participantIds, watcherIds);
    }

    public void validParticipantNumber(final int participantNumber) {
        if (participantNumber <= interviewerNumber) {
            throw new InvalidFieldException("참가자 수는 인터뷰어 수 보다 많아야 합니다.", DebugMessage.init()
                    .append("participantNumber", participantNumber)
                    .append("interviewerNumber", interviewerNumber));
        }
    }

    public void close(final LocalDateTime presentTime) {
        validateInProgress(presentTime);

        isClosed = true;
    }

    public void validateInProgress(final LocalDateTime presentTime) {
        if (presentTime.isBefore(startAt)) {
            throw new TeamNotInProgressException(DebugMessage.init()
                    .append("teamId", getId())
                    .append("isClosed", isClosed)
                    .append("startAt", startAt)
                    .append("presentTime", presentTime));
        }
        if (isClosed) {
            throw new TeamAlreadyClosedException(DebugMessage.init()
                    .append("teamId", getId()));
        }
    }

    public void validateReady(final LocalDateTime presentTime) {
        if (presentTime.isAfter(this.startAt)) {
            throw new TeamNotReadyException(DebugMessage.init()
                    .append("teamId", getId())
                    .append("startAt", startAt)
                    .append("presentTime", presentTime));
        }
    }

    public void delete(final LocalDateTime presentTime) {
        validateReady(presentTime);

        this.deleted = true;
    }

    public TeamStatus status(final LocalDateTime presentTime) {
        if (isClosed) {
            return TeamStatus.CLOSED;
        }
        if (startAt.isAfter(presentTime)) {
            return TeamStatus.READY;
        }
        return TeamStatus.IN_PROGRESS;
    }

    public void addParticipants(final Long hostId, final List<Long> participantIds, final List<Long> watcherIds) {
        validateParticipants(hostId, participantIds, watcherIds);

        this.participants = Participants.of(this, hostId, participantIds, watcherIds);
    }

    public void updateParticipants(final Long hostId, final List<Long> participantIds, final List<Long> watcherIds) {
        validateParticipants(hostId, participantIds, watcherIds);

        final Participants updateParticipants = Participants.of(this, hostId, participantIds, watcherIds);
        updateParticipants.getValues().forEach(it -> this.participants.getValues().add(it));
    }

    private void validateParticipants(final Long hostId, final List<Long> participantIds, final List<Long> watcherIds) {
        validateParticipantExistence(participantIds);
        validateDistinctParticipant(participantIds);
        validateDistinctWatcher(watcherIds);
        validateIndependent(participantIds, watcherIds);
        validateHostExistence(hostId, participantIds, watcherIds);
        validParticipantNumber(participantIds.size());
    }

    private void validateParticipantExistence(final List<Long> participantsIds) {
        if (participantsIds.isEmpty()) {
            throw new InvalidFieldException("참가자가 존재하지 않습니다.", DebugMessage.init()
                    .append("participants", participantsIds));
        }
    }

    private void validateDistinctParticipant(final List<Long> participantIds) {
        final Set<Long> distinct = new HashSet<>(participantIds);
        if (distinct.size() != participantIds.size()) {
            throw new InvalidFieldException("중복된 참가자가 존재합니다.", DebugMessage.init()
                    .append("participants", participantIds));
        }
    }

    private void validateDistinctWatcher(final List<Long> watcherIds) {
        final Set<Long> distinct = new HashSet<>(watcherIds);
        if (distinct.size() != watcherIds.size()) {
            throw new InvalidFieldException("중복된 참관자가 존재합니다.", DebugMessage.init()
                    .append("watchers", watcherIds));
        }
    }

    private void validateIndependent(final List<Long> participantIds, final List<Long> watcherIds) {
        final boolean notIndependent = participantIds.stream()
                .anyMatch(watcherIds::contains);

        if (notIndependent) {
            throw new InvalidFieldException("참가자와 참관자에 모두 포함된 멤버가 존재합니다.", DebugMessage.init()
                    .append("particiapnts", participantIds)
                    .append("watchers", watcherIds));
        }
    }

    private void validateHostExistence(final Long hostId, final List<Long> participantIds,
                                       final List<Long> watcherIds) {
        if (!participantIds.contains(hostId) && !watcherIds.contains(hostId)) {
            throw new InvalidFieldException("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.", DebugMessage.init()
                    .append("hostId", hostId)
                    .append("participants", participantIds)
                    .append("watchers", watcherIds));
        }
    }
}
