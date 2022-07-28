package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public void updateContent(final String content) {
        this.content = content;
    }

    public boolean isAuthorId(final Long memberId) {
        return author.getId().equals(memberId);
    }

    public void validateSelfFeedback(final Member member) {
        if (isAuthorId(member.getId())) {
            throw new InvalidFeedbackException("자기 자신에게 피드백을 할 수 없습니다.");
        }
    }
}
