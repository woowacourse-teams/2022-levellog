package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
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

    public static final int FEEDBACK_CONTENT_MAX_LENGTH = 1000;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_from_member"))
    private Member from;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_to_member"))
    private Member to;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_levellog"))
    private Levellog levellog;

    @Column(length = 1000)
    private String study;

    @Column(length = 1000)
    private String speak;

    @Column(length = 1000)
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
        validateFeedbackLength(study);
        validateFeedbackLength(speak);
        validateFeedbackLength(etc);
    }

    private void validateFeedbackLength(final String feedbackContent) {
        if (feedbackContent.length() > FEEDBACK_CONTENT_MAX_LENGTH) {
            throw new InvalidFieldException("피드백은 " + FEEDBACK_CONTENT_MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    public void updateFeedback(final String study, final String speak, final String etc) {
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    public boolean isAssociatedWith(final Member member) {
        return from.equals(member) || to.equals(member);
    }
}
