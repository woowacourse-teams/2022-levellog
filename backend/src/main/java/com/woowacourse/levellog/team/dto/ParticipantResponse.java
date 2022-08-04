<<<<<<<< HEAD:backend/src/main/java/com/woowacourse/levellog/authentication/dto/LoginDto.java
package com.woowacourse.levellog.authentication.dto;
========
package com.woowacourse.levellog.team.dto;
>>>>>>>> main:backend/src/main/java/com/woowacourse/levellog/team/dto/ParticipantResponse.java

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
    private String profileUrl;
}
