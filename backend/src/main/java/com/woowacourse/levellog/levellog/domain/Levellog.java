package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
<<<<<<< HEAD
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
=======
import com.woowacourse.levellog.member.domain.Member;
>>>>>>> main
import com.woowacourse.levellog.team.domain.Team;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
<<<<<<< HEAD
=======
import lombok.AllArgsConstructor;
>>>>>>> main
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
=======
@AllArgsConstructor
>>>>>>> main
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

<<<<<<< HEAD
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

    private void validateAuthor(final Member member, final String errorMessage) {
        final boolean isNotAuthor = !author.equals(member);
        if (isNotAuthor) {
            throw new UnauthorizedException(errorMessage);
        }
    }

    public void updateContent(final Member member, final String content) {
        validateAuthor(member, "레벨로그를 수정할 권한이 없습니다. memberId : " + member.getId() + " levellogId : " + getId());
        validateContent(content);
        this.content = content;
    }

    public boolean isAuthor(final Member member) {
        return author.equals(member);
    }

    public void validateSelfFeedback(final Member member) {
        if (isAuthor(member)) {
            throw new InvalidFeedbackException(" [levellogId : " + getId() + "]", "자기 자신에게 피드백을 할 수 없습니다.");
        }
    }

    public void validateSelfPreQuestion(final Member member) {
        if (isAuthor(member)) {
            throw new InvalidPreQuestionException(" [levellogId : " + getId() + "]", "자기 자신에게 사전 질문을 등록할 수 없습니다.");
        }
=======
    public void updateContent(final String content) {
        this.content = content;
    }

    public boolean isAuthorId(final Long memberId) {
        return author.getId().equals(memberId);
>>>>>>> main
    }
}
