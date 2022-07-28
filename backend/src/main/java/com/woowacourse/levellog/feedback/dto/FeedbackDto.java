package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackDto {

    private Long id;
    private MemberDto from;
    private FeedbackContentDto feedback;
    private LocalDateTime updatedAt;

    public static FeedbackDto from(final Feedback feedback) {
        return new FeedbackDto(feedback.getId(), MemberDto.from(feedback.getFrom()), FeedbackContentDto.from(feedback),
                feedback.getUpdatedAt());
    }
}
