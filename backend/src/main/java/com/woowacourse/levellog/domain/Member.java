package com.woowacourse.levellog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member_github_id", columnNames = {"githubId"})})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private Integer githubId;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public Member(final String nickname, final Integer githubId, final String profileUrl) {
        this.nickname = nickname;
        this.githubId = githubId;
        this.profileUrl = profileUrl;
    }

    public void updateProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }
}