package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class TeamDetail {

    private static final int PROFILE_URL_SIZE = 2048;
    private static final int DEFAULT_STRING_SIZE = 255;
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

    public TeamDetail(final String title, final String place, final LocalDateTime startAt, final String profileUrl,
                      final int interviewerNumber) {
        validate(title, place, startAt, profileUrl, interviewerNumber);

        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.profileUrl = profileUrl;
        this.interviewerNumber = interviewerNumber;
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
}
