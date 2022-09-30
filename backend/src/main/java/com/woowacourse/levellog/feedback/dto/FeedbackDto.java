package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.member.dto.MemberDto;
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
public class FeedbackDto {

    private Long id;
    private MemberDto from;
    private MemberDto to;
    private FeedbackContentDto feedback;
    private LocalDateTime updatedAt;
}
