package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.team.domain.Team;
import javax.persistence.Column;
import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_levellog_author"))
    private Member author;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_levellog_team"))
    private Team team;

    @Column(nullable = false)
    @Lob
    private String content;

    private Levellog(final Member author, final Team team, final String content) {
        validateContent(content);
        this.author = author;
        this.team = team;
        this.content = content;
    }

    public static Levellog of(final Member author, final Team team, final String content) {
        return new Levellog(author, team, content);
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }

    private void validateAuthor(final Member member) {
        final boolean isNotAuthor = !author.equals(member);
        if (isNotAuthor) {
            throw new MemberNotAuthorException(DebugMessage.init()
                    .append("loginMemberId", member.getId())
                    .append("authorMemberId", author.getId())
                    .append("levellogId", getId())
            );
        }

    }

    public void updateContent(final Member member, final String content) {
        validateAuthor(member);
        validateContent(content);
        this.content = content;
    }

    public boolean isAuthor(final Member member) {
        return author.equals(member);
    }

    public void validateSelfFeedback(final Member member) {
        if (isAuthor(member)) {
            throw new InvalidFeedbackException(DebugMessage.init()
                    .append("levellogId", getId()));
        }
    }

    public void validateSelfPreQuestion(final Member member) {
        if (isAuthor(member)) {
            throw new InvalidPreQuestionException(DebugMessage.init()
                    .append("levellogAuthorId", getAuthor().getId())
                    .append("preQuestionAuthorId", member.getId()));
        }
    }

    public void validateSelfInterviewQuestion(final Member member) {
        if (isAuthor(member)) {
            throw new InvalidInterviewQuestionException(DebugMessage.init()
                    .append("levellogId", getId())
                    .append("memberId", member.getId()));
        }
    }
}
