package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
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
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_from_member"))
    private Member from;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_to_member"))
    private Member to;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_interview_question_levellog"))
    private Levellog levellog;

    @Column(nullable = false)
    private String content;

    private InterviewQuestion(final Member from, final Member to, final Levellog levellog, final String content) {
        validateContent(content);

        this.from = from;
        this.to = to;
        this.levellog = levellog;
        this.content = content;
    }

    public static InterviewQuestion of(final Member from, final Member to, final Levellog levellog,
                                       final String content) {
        return new InterviewQuestion(from, to, levellog, content);
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }
        if (content.length() > DEFAULT_STRING_SIZE) {
            throw new InvalidFieldException("인터뷰 질문은 " + DEFAULT_STRING_SIZE + "자 이하여야합니다. 현재 길이:" + content.length());
        }
    }

    public void validateAuthor(final Member member) {
        final boolean isNotAuthor = !from.equals(member);
        if (isNotAuthor) {
            throw new UnauthorizedException(
                    "인터뷰 질문에 대한 쓰기 권한이 없습니다. 현재 memberId : " + member.getId() + " 인터뷰 질문 작성자 memberId  : "
                            + from.getId() + " levellogId : " + levellog.getId());
        }
    }

    public void updateContent(final String content, final Member member) {
        validateContent(content);
        validateAuthor(member);

        this.content = content;
    }
}
