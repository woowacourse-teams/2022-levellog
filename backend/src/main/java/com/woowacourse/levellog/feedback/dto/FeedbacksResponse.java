<<<<<<<< HEAD:backend/src/main/java/com/woowacourse/levellog/member/dto/MembersDto.java
package com.woowacourse.levellog.member.dto;
========
package com.woowacourse.levellog.feedback.dto;
>>>>>>>> main:backend/src/main/java/com/woowacourse/levellog/feedback/dto/FeedbacksResponse.java

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode
public class MembersDto {

    private List<MemberDto> members;
}
