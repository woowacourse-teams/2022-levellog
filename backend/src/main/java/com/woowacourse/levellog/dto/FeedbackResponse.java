package com.woowacourse.levellog.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FeedbackResponse {

    private Long id;
    private MemberResponse from;
    private FeedbackContentDto feedback;
    private LocalDateTime updatedAt;
}
