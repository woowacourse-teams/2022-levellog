package com.woowacourse.levellog.member.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
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

    public static final int NICKNAME_MAX_LENGTH = 50;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private Integer githubId;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public void updateNickname(final String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(final String nickname) {
        validateBlank(nickname);
        validateNicknameLength(nickname);
    }

    private void validateBlank(final String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new InvalidFieldException("닉네임은 공백이나 null일 수 없습니다.");
        }
    }

    private void validateNicknameLength(final String nickname) {
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new InvalidFieldException("닉네임은 " + NICKNAME_MAX_LENGTH + "자 이하여야합니다.");
        }
    }
}
