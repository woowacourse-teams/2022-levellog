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

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String study;

    @Column(length = 1000)
    private String speak;

    @Column(length = 1000)
    private String etc;

    public Feedback(final String name, final String study, final String speak, final String etc) {
        this.name = name;
        this.study = study;
        this.speak = speak;
        this.etc = etc;
    }
}
