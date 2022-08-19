package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.common.support.DebugMessage;
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
public class InterviewQuestion extends BaseEntity {

    private static final int DEFAULT_STRING_SIZE = 255;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_author_member"))
    private Member author;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_levellog"))
    private Levellog levellog;

    @Column(nullable = false)
    private String content;

    private InterviewQuestion(final Member author, final Levellog levellog, final String content) {
        validateContent(content);

        this.author = author;
        this.levellog = levellog;
        this.content = content;
    }

    public static InterviewQuestion of(final Member author, final Levellog levellog,
                                       final String content) {
        return new InterviewQuestion(author, levellog, content);
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }
        if (content.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("인터뷰 질문은 " + DEFAULT_STRING_SIZE + "자 이하여야합니다. 현재 길이:" + content.length());
        }
    }

    public void validateMemberIsAuthor(final Member member) {
        final boolean isNotAuthor = !author.equals(member);
        if (isNotAuthor) {
            throw new MemberNotAuthorException(DebugMessage.init()
                    .append("loginMemberId", member.getId())
                    .append("authorMemberId", author.getId())
                    .append("interviewQuestionId", getId()));
        }
    }

    public boolean isAuthor(final Member member) {
        return author.equals(member);
    }

    public void updateContent(final String content, final Member member) {
        validateContent(content);
        validateMemberIsAuthor(member);

        this.content = content;
    }
}
