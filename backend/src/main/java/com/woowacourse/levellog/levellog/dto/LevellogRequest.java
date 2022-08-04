<<<<<<<< HEAD:backend/src/main/java/com/woowacourse/levellog/authentication/dto/GithubCodeDto.java
package com.woowacourse.levellog.authentication.dto;
========
package com.woowacourse.levellog.levellog.dto;
>>>>>>>> main:backend/src/main/java/com/woowacourse/levellog/levellog/dto/LevellogRequest.java

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GithubCodeDto {

    @NotBlank
    private String authorizationCode;
}
