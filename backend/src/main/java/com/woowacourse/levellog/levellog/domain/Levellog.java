package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.team.domain.Team;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Levellog extends BaseEntity {

    @Column(nullable = false)
    private Long authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_levellog_team"))
    private Team team;

    @Column(nullable = false)
    @Lob
    private String content;

    public Levellog(final Long authorId, final Team team, final String content) {
        validateContent(content);
        this.authorId = authorId;
        this.team = team;
        this.content = content;
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("레벨로그 내용은 공백이나 null일 수 없습니다.",
                    DebugMessage.init()
                            .append("content", content));
        }
    }

    private void validateAuthor(final Long memberId) {
        final boolean isNotAuthor = !authorId.equals(memberId);
        if (isNotAuthor) {
            throw new MemberNotAuthorException(DebugMessage.init()
                    .append("loginMemberId", memberId)
                    .append("authorMemberId", authorId)
                    .append("levellogId", getId())
            );
        }
    }

    public void updateContent(final Long memberId, final String content) {
        validateAuthor(memberId);
        validateContent(content);
        this.content = content;
    }

    public boolean isAuthor(final Long memberId) {
        return authorId.equals(memberId);
    }

    public void validateSelfFeedback(final Long memberId) {
        if (isAuthor(memberId)) {
            throw new InvalidFeedbackException(DebugMessage.init()
                    .append("levellogId", getId()));
        }
    }

    public void validateSelfPreQuestion(final Long memberId) {
        if (isAuthor(memberId)) {
            throw new InvalidPreQuestionException(DebugMessage.init()
                    .append("levellogAuthorId", authorId)
                    .append("preQuestionAuthorId", memberId));
        }
    }

    public void validateSelfInterviewQuestion(final Long memberId) {
        if (isAuthor(memberId)) {
            throw new InvalidInterviewQuestionException(DebugMessage.init()
                    .append("levellogId", getId())
                    .append("memberId", memberId));
        }
    }
}
