package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
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
public class InterviewQuestion extends BaseEntity {

    private static final int DEFAULT_STRING_SIZE = 255;

    @Column(nullable = false)
    private Long authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_levellog"))
    private Levellog levellog;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int likeCount = 0;

    public InterviewQuestion(final Long authorId, final Levellog levellog, final String content) {
        validateContent(content);

        this.authorId = authorId;
        this.levellog = levellog;
        this.content = content;
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("인터뷰 질문은 공백이나 null일 수 없습니다.", DebugMessage.init()
                    .append("content", content));
        }
        if (content.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("인터뷰 질문은 " + DEFAULT_STRING_SIZE + "자 이하여야합니다.", DebugMessage.init()
                    .append("content 길이", content.length()));
        }
    }

    public void validateMemberIsAuthor(final Long memberId) {
        final boolean isNotAuthor = !authorId.equals(memberId);
        if (isNotAuthor) {
            throw new MemberNotAuthorException(DebugMessage.init()
                    .append("loginMemberId", memberId)
                    .append("authorMemberId", authorId)
                    .append("interviewQuestionId", getId()));
        }
    }

    public void updateContent(final String content, final Long memberId) {
        validateContent(content);
        validateMemberIsAuthor(memberId);

        this.content = content;
    }

    public void upLike() {
        this.likeCount++;
    }

    public void downLike() {
        this.likeCount--;
    }
}
