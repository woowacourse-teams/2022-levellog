package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.InvalidLevellogException;
import com.woowacourse.levellog.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback extends BaseEntity {

    public static final String CONTENT_TYPE_SPEAK = "Speak";
    public static final String CONTENT_TYPE_ETC = "Etc";
    private static final int FEEDBACK_CONTENT_MAX_LENGTH = 1000;
    private static final String CONTENT_TYPE_STUDY = "Study";

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_from_member"))
    private Member from;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_to_member"))
    private Member to;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_levellog"))
    private Levellog levellog;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String study;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String speak;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String etc;

    public Feedback(final Member from, final Member to, final Levellog levellog, final String study, final String speak,
                    final String etc) {
        validateFeedback(study, speak, etc);

        this.from = from;
        this.to = to;
        this.levellog = levellog;
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    private void validateFeedback(final String study, final String speak, final String etc) {
        validateFeedbackLength(study, CONTENT_TYPE_STUDY);
        validateFeedbackLength(speak, CONTENT_TYPE_SPEAK);
        validateFeedbackLength(etc, CONTENT_TYPE_ETC);
    }

    private void validateFeedbackLength(final String feedbackContent, final String contentType) {
        if (feedbackContent.length() > FEEDBACK_CONTENT_MAX_LENGTH) {
            throw new InvalidFieldException(contentType + " 피드백은 " + FEEDBACK_CONTENT_MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    public void updateFeedback(final String study, final String speak, final String etc) {
        validateFeedback(study, speak, etc);

        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    public void validateAuthor(final Member member) {
        if (!from.equals(member)) {
            throw new InvalidFeedbackException("자신이 남긴 피드백만 수정할 수 있습니다.",
                    " [feedbackId : " + getId() + ", memberId : " + member.getId() + "]");
        }
    }

    public void validateLevellog(final Levellog levellog) {
        if (!isSameLevellog(levellog)) {
            throw new InvalidLevellogException(
                    "입력한 levellogId와 피드백의 levellogId가 다릅니다.", "[ 입력한 levellogId : " + levellog.getId() + " ] ");
        }
    }

    private boolean isSameLevellog(final Levellog levellog) {
        return this.levellog.equals(levellog);
    }
}
