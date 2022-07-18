package com.woowacourse.levellog.domain;

import com.woowacourse.levellog.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member_github_id", columnNames = {"githubId"})})
public class Member extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private Integer githubId;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public void updateProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
