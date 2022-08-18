package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import java.time.LocalDateTime;
import javax.persistence.Column;
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
            throw new InvalidFieldException("팀 이름이 null 또는 공백입니다. 입력한 팀 이름 : [" + title + "]");
        }
        if (title.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("잘못된 팀 이름을 입력했습니다. 입력한 팀 이름 : [" + title + "]");
        }
    }

    private void validatePlace(final String place) {
        if (place == null || place.isBlank()) {
            throw new InvalidFieldException("장소가 null 또는 공백입니다. 입력한 장소 : [" + place + "]");
        }
        if (place.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("잘못된 장소를 입력했습니다. 입력한 장소 : [" + place + "]");
        }
    }

    private void validateStartAt(final LocalDateTime startAt) {
        if (startAt == null) {
            throw new InvalidFieldException("시작 시간이 없습니다.");
        }

        final LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(startAt)) {
            throw new InvalidFieldException("인터뷰 시작 시간은 현재 시간 이후여야 합니다.");
        }
    }

    private void validateProfileUrl(final String profileUrl) {
        if (profileUrl == null || profileUrl.isBlank()) {
            throw new InvalidFieldException("팀 프로필 사진이 null 또는 공백입니다. 입력한 프로필 URL : [" + profileUrl + "]");
        }
        if (profileUrl.length() > PROFILE_URL_SIZE) {
            throw new InvalidFieldException("잘못된 팀 프로필 사진을 입력했습니다. 입력한 프로필 URL : [" + profileUrl + "]");
        }
    }

    private void validateInterviewerNumber(final int interviewerNumber) {
        if (interviewerNumber < MIN_INTERVIEWER_NUMBER) {
            throw new InvalidFieldException("팀 생성시 인터뷰어 수는 " + MIN_INTERVIEWER_NUMBER + "명 이상이어야 합니다. "
                    + "입력한 인터뷰어 수 : [" + interviewerNumber + "]");
        }
    }

    public void update(final Team team, final LocalDateTime presentTime) {
        validateTeamBeforeStartAt(presentTime, "인터뷰가 시작된 이후에는 수정할 수 없습니다.");

        this.title = team.title;
        this.place = team.place;
        this.startAt = team.startAt;
        this.profileUrl = team.profileUrl;
        this.interviewerNumber = team.interviewerNumber;
    }

    public void validParticipantNumber(final int participantNumber) {
        if (participantNumber <= interviewerNumber) {
            throw new InvalidFieldException("참가자 수는 인터뷰어 수 보다 많아야 합니다.");
        }
    }

    private void validateTeamBeforeStartAt(final LocalDateTime presentTime, final String errorMessage) {
        if (presentTime.isAfter(this.startAt)) {
            throw new InterviewTimeException(errorMessage, getId(), startAt, presentTime);
        }
    }

    public void close(final LocalDateTime presentTime) {
        validateInProgress(presentTime, "인터뷰가 시작되기 전에 종료할 수 없습니다.");

        isClosed = true;
    }

    public void validateInProgress(final LocalDateTime presentTime, final String message) {
        if (presentTime.isBefore(startAt)) {
            throw new InterviewTimeException(message, getId(), startAt, presentTime);
        }
        if (isClosed) {
            throw new TeamAlreadyClosedException(DebugMessage.init()
                    .append("teamId", getId()));
        }
    }

    public void validateReady(final LocalDateTime presentTime, final String message) {
        if (presentTime.isAfter(startAt)) {
            throw new InterviewTimeException(message, getId(), startAt, presentTime);
        }
    }

    public void delete(final LocalDateTime presentTime) {
        validateTeamBeforeStartAt(presentTime, "인터뷰가 시작된 이후에는 삭제할 수 없습니다.");

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

    public boolean isSameStatus(final String status, final LocalDateTime presentTime) {
        return status(presentTime).isSame(status);
    }
}
