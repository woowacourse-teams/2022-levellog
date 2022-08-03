package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
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
public class PreQuestion extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_pre_question_levellog"))
    private Levellog levellog;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_pre_question_from_member"))
    private Member author;

    @Column(nullable = false)
    @Lob
    private String content;

    public PreQuestion(final Levellog levellog, final Member author, final String content) {
        validateIsAuthor(levellog, author);
        validateContent(content);

        this.levellog = levellog;
        this.author = author;
        this.content = content;
    }

    private void validateIsAuthor(final Levellog levellog, final Member member) {
        if (levellog.isAuthor(member)) {
            throw new InvalidFieldException("내 레벨로그에 사전 질문을 작성할 수 없습니다.");
        }
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다.");
        }
    }

    public void update(final String content) {
        validateContent(content);

        this.content = content;
    }

    public boolean isSameLevellog(final Levellog levellog) {
        return this.levellog.equals(levellog);
    }

    public boolean isSameAuthor(final Member member) {
        return author.equals(member);
    }
}