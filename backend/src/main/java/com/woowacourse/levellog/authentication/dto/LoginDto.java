package com.woowacourse.levellog.authentication.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class LoginDto {

    private Long id;
    private String accessToken;
    private String nickname;
    private String profileUrl;
}
