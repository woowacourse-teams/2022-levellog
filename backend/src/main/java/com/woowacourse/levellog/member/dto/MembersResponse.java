<<<<<<<< HEAD:backend/src/main/java/com/woowacourse/levellog/feedback/dto/FeedbacksDto.java
package com.woowacourse.levellog.feedback.dto;
========
package com.woowacourse.levellog.member.dto;
>>>>>>>> main:backend/src/main/java/com/woowacourse/levellog/member/dto/MembersResponse.java

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbacksDto {

    private List<FeedbackDto> feedbacks;
}
