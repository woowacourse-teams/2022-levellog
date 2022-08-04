package com.woowacourse.levellog.member.domain;

<<<<<<< HEAD
import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
=======
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.member.exception.InvalidFieldException;
>>>>>>> main
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
<<<<<<< HEAD
=======
import lombok.AllArgsConstructor;
>>>>>>> main
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
<<<<<<< HEAD
=======
@AllArgsConstructor
>>>>>>> main
@Getter
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member_github_id", columnNames = {"githubId"})})
public class Member extends BaseEntity {

<<<<<<< HEAD
    private static final int NICKNAME_MAX_LENGTH = 50;
    private static final int PROFILE_URL_MAX_LENGTH = 2048;

    @Column(nullable = false, length = NICKNAME_MAX_LENGTH)
=======
    public static final int NICKNAME_MAX_LENGTH = 50;

    @Column(nullable = false, length = 50)
>>>>>>> main
    private String nickname;

    @Column(nullable = false)
    private Integer githubId;

<<<<<<< HEAD
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
        checkBlank(nickname, "닉네임은 공백이나 null일 수 없습니다.");
        checkLength(nickname, NICKNAME_MAX_LENGTH,
                "닉네임은 " + NICKNAME_MAX_LENGTH + "자 이하여야합니다. [현재 길이:" + nickname.length() + "]");
    }

    private void validateGithubId(final Integer githubId) {
        checkNull(githubId, "깃허브 ID는 null일 수 없습니다.");
    }

    private void validateProfileUrl(final String profileUrl) {
        checkBlank(profileUrl, "프로필 url은 공백이나 null일 수 없습니다.");
        checkLength(profileUrl, PROFILE_URL_MAX_LENGTH,
                "프로필 url은 " + PROFILE_URL_MAX_LENGTH + "자 이하여야합니다. [현재 길이:" + profileUrl.length() + "]");
    }

    private void checkLength(final String target, final int length, final String message) {
        if (target.length() > length) {
            throw new InvalidFieldException(message);
        }
    }

    private void checkBlank(final String target, final String message) {
        if (target == null || target.isBlank()) {
            throw new InvalidFieldException(message);
        }
    }

    private void checkNull(final Object target, final String message) {
        if (target == null) {
            throw new InvalidFieldException(message);
        }
    }

=======
    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public void updateProfileUrl(final String profileUrl) {
        this.profileUrl = profileUrl;
    }

>>>>>>> main
    public void updateNickname(final String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }
<<<<<<< HEAD
=======

    private void validateNickname(final String nickname) {
        validateBlank(nickname);
        validateNicknameLength(nickname);
    }

    private void validateBlank(final String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new InvalidFieldException("닉네임은 공백이나 null일 수 없습니다.", BAD_REQUEST);
        }
    }

    private void validateNicknameLength(final String nickname) {
        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new InvalidFieldException("닉네임은 " + NICKNAME_MAX_LENGTH + "자 이하여야합니다.", BAD_REQUEST);
        }
    }
>>>>>>> main
}
