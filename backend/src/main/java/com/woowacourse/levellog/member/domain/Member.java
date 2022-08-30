package com.woowacourse.levellog.member.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member_github_id", columnNames = {"githubId"})})
public class Member extends BaseEntity {

    private static final int NICKNAME_MAX_LENGTH = 50;
    private static final int PROFILE_URL_MAX_LENGTH = 2048;

    @Column(nullable = false, length = NICKNAME_MAX_LENGTH)
    private String nickname;

    @Column(nullable = false)
    private Integer githubId;

    @Column(nullable = false, length = PROFILE_URL_MAX_LENGTH)
    private String profileUrl;

    public Member(final String nickname, final Integer githubId, final String profileUrl) {
        validateNickname(nickname);
        validateGithubId(githubId);
        validateProfileUrl(profileUrl);

        this.nickname = nickname;
        this.githubId = githubId;
        this.profileUrl = profileUrl;
    }

    private void validateNickname(final String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new InvalidFieldException("닉네임은 공백이나 null일 수 없습니다.", DebugMessage.init()
                    .append("nickname", nickname));
        }
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new InvalidFieldException("닉네임은 " + NICKNAME_MAX_LENGTH + "자 이하여야합니다.", DebugMessage.init()
                    .append("nickname 길이", nickname.length()));
        }
    }

    private void validateGithubId(final Integer githubId) {
        if (githubId == null) {
            throw new InvalidFieldException("깃허브 ID는 null일 수 없습니다.", DebugMessage.init()
                    .append("githubId", githubId));
        }
    }

    private void validateProfileUrl(final String profileUrl) {
        if (profileUrl == null || profileUrl.isBlank()) {
            throw new InvalidFieldException("프로필 url은 공백이나 null일 수 없습니다.", DebugMessage.init()
                    .append("profileUrl", profileUrl));
        }
        if (profileUrl.length() > PROFILE_URL_MAX_LENGTH) {
            throw new InvalidFieldException("프로필 url은 " + PROFILE_URL_MAX_LENGTH + "자 이하여야합니다.", DebugMessage.init()
                    .append("profileUrl 길이", profileUrl.length()));
        }
    }

    public void updateNickname(final String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }
}
