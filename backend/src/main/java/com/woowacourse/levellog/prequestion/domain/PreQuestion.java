package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.InvalidLevellogException;
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
        validateSelfPreQuestion(levellog, author);
        validateContent(content);

        this.levellog = levellog;
        this.author = author;
        this.content = content;
    }

    private void validateSelfPreQuestion(final Levellog levellog, final Member member) {
        levellog.validateSelfPreQuestion(member);
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다.", DebugMessage.init()
                    .append("content", content));
        }
    }

    public void update(final String content) {
        validateContent(content);

        this.content = content;
    }

    private boolean isSameLevellog(final Levellog levellog) {
        return this.levellog.equals(levellog);
    }

    public boolean isSameAuthor(final Member member) {
        return author.equals(member);
    }

    public void validateLevellog(final Levellog levellog) {
        if (!isSameLevellog(levellog)) {
            throw new InvalidLevellogException(DebugMessage.init()
                    .append("Feedback's levellogId'", this.levellog.getId())
                    .append("Input levellogId", levellog.getId()));
        }
    }

    public void validateMyQuestion(final Member member) {
        if (!isSameAuthor(member)) {
            throw new MemberNotAuthorException(DebugMessage.init()
                    .append("loginMemberId", member.getId())
                    .append("authorMemberId", author.getId())
                    .append("preQuestionId", getId()));
        }
    }
}
