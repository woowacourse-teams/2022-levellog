package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.member.dto.MemberDto;
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
    private MemberDto from;
    private FeedbackContentDto feedback;
    private LocalDateTime updatedAt;

    public static FeedbackResponse from(Feedback feedback) {
        return new FeedbackResponse(feedback.getId(), MemberDto.from(feedback.getFrom()), FeedbackContentDto.from(feedback), feedback.getUpdatedAt());
    }
}
