package com.woowacourse.levellog.feedback.dto.response;

import com.woowacourse.levellog.feedback.dto.request.FeedbackContentRequest;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackResponse {

    private Long id;
    private MemberResponse from;
    private MemberResponse to;
    private FeedbackContentRequest feedback;
    private LocalDateTime updatedAt;
}
