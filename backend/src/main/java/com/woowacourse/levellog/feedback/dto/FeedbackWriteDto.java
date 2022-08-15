package com.woowacourse.levellog.feedback.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackWriteDto {

    @NotNull
    @Valid
    private FeedbackContentDto feedback;

    public static FeedbackWriteDto from(final String study, final String speak, final String etc) {
        return new FeedbackWriteDto(new FeedbackContentDto(study, speak, etc));
    }
}
