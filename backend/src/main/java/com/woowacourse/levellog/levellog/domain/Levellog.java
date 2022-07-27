package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.domain.Member;
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

    public Levellog(final Member author, final Team team, final String content) {
        validate(author, team, content);
        this.author = author;
        this.team = team;
        this.content = content;
    }

    private void validate(final Member author, final Team team, final String content) {
        validateAuthor(author);
        validateTeam(team);
        validateContent(content);
    }

    private void validateAuthor(final Member author) {
        if (author == null) {
            throw new InvalidFieldException("레벨로그의 작성자는 null일 수 없습니다.");
        }
    }

    private void validateTeam(final Team team) {
        if (team == null) {
            throw new InvalidFieldException("레벨로그의 팀은 null일 수 없습니다.");
        }
    }

    private void validateContent(final String content) {
        if (content == null || content.isBlank()) {
            throw new InvalidFieldException("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }

    public void updateContent(final String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isAuthorId(final Long memberId) {
        return author.getId().equals(memberId);
    }
}
