package com.woowacourse.levellog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_from_member"))
    private Member from;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_to_member"))
    private Member to;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_feedback_levellog"))
    private Levellog levellog;

    @Column(length = 1000)
    private String study;

    @Column(length = 1000)
    private String speak;

    @Column(length = 1000)
    private String etc;

    public Feedback(final Member from, final Member to, final Levellog levellog, final String study, final String speak,
                    final String etc) {
        this.from = from;
        this.to = to;
        this.levellog = levellog;
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }

    public void updateFeedback(final String study, final String speak, final String etc) {
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }
}
