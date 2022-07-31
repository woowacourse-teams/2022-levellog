package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
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
    private Member from;

    @Lob
    private String preQuestion;

    public PreQuestion(final Levellog levellog, final Member from, final String preQuestion) {
        validateIsAuthor(levellog, from);
        validateQuestion(preQuestion);

        this.levellog = levellog;
        this.from = from;
        this.preQuestion = preQuestion;
    }

    private void validateIsAuthor(final Levellog author, final Member member) {
        if (author.isAuthor(member)) {
            throw new InvalidFieldException("내 글의 사전 질문을 작성할 수 없습니다.");
        }
    }

    private void validateQuestion(final String question) {
        if (question == null || question.isBlank()) {
            throw new InvalidFieldException("사전 내용은 공백이나 null일 수 없습니다.");
        }
    }

    public void update(final String preQuestion) {
        validateQuestion(preQuestion);
    }

    public boolean isSameLevellog(final Levellog levellog) {
        return this.levellog.equals(levellog);
    }
}
