package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import javax.persistence.Column;
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
public class Feedback extends BaseEntity {

    private static final int FEEDBACK_CONTENT_MAX_LENGTH = 1000;
    private static final String CONTENT_TYPE_SPEAK = "Speak";
    private static final String CONTENT_TYPE_ETC = "Etc";
    private static final String CONTENT_TYPE_STUDY = "Study";

    @Column(nullable = false)
    private Long fromId;

    @Column(nullable = false)
    private Long toId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_levellog"))
    private Levellog levellog;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String study;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String speak;

    @Column(length = FEEDBACK_CONTENT_MAX_LENGTH)
    private String etc;

    public Feedback(final Long fromId, final Levellog levellog, final String study, final String speak,
                    final String etc) {
        validateFeedback(study, speak, etc);

        this.fromId = fromId;
        this.toId = levellog.getAuthorId();
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
            throw new InvalidFieldException(contentType + " 피드백은 " + FEEDBACK_CONTENT_MAX_LENGTH + "자 이하여야 합니다.",
                    DebugMessage.init()
                            .append("content 길이", feedbackContent.length()));
        }
    }

    public void updateFeedback(final String study, final String speak, final String etc) {
        validateFeedback(study, speak, etc);

        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    public void validateAuthor(final Long memberId) {
        if (!fromId.equals(memberId)) {
            throw new InvalidFeedbackException(DebugMessage.init()
                    .append("feedbackId", getId())
                    .append("memberId", memberId));
        }
    }
}
