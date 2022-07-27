package com.woowacourse.levellog.feedback.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateFeedbackDto {

    @NotNull
    @Valid
    private FeedbackContentDto feedback;
}
