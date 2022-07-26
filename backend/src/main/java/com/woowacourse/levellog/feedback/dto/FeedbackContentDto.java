package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.feedback.domain.Feedback;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackContentDto {

    @NotNull
    private String study;

    @NotNull
    private String speak;

    @NotNull
    private String etc;

    public static FeedbackContentDto from(Feedback feedback) {
        return new FeedbackContentDto(feedback.getStudy(), feedback.getSpeak(), feedback.getEtc());
    }
}
