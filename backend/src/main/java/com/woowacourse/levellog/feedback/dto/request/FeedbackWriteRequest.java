package com.woowacourse.levellog.feedback.dto.request;

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
public class FeedbackWriteRequest {

    @NotNull
    @Valid
    private FeedbackContentRequest feedback;

    public static FeedbackWriteRequest from(final String study, final String speak, final String etc) {
        return new FeedbackWriteRequest(new FeedbackContentRequest(study, speak, etc));
    }
}
