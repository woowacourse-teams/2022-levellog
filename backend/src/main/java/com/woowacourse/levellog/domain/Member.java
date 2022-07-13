package com.woowacourse.levellog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true)
    private Integer githubId;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public Member(final String nickname, final Integer githubId, final String profileUrl) {
        this.nickname = nickname;
        this.githubId = githubId;
        this.profileUrl = profileUrl;
    }
}
