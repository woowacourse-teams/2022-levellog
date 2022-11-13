package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_authorId_teamId",
                columnNames = {"fromId", "levellog_id"}
        )})
public class Feedback extends BaseEntity {

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

    @Lob
    private String study;

    @Lob
    private String speak;

    @Lob
    private String etc;

    public Feedback(final Long fromId, final Levellog levellog, final String study, final String speak,
                    final String etc) {
        this.fromId = fromId;
        this.toId = levellog.getAuthorId();
        this.levellog = levellog;
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    public void updateFeedback(final String study, final String speak, final String etc) {
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
