package com.woowacourse.levellog.common.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class LoginStatus {

    private final Long memberId;

    private LoginStatus(final Long memberId) {
        this.memberId = memberId;
    }

    public static LoginStatus fromNotLogin() {
        return new LoginStatus(null);
    }

    public static LoginStatus fromLogin(final Long memberId) {
        return new LoginStatus(memberId);
    }

    public boolean isLogin() {
        return memberId != null;
    }
}
