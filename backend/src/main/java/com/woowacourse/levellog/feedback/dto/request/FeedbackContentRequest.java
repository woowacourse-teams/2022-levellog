package com.woowacourse.levellog.feedback.dto.request;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
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
public class FeedbackContentRequest {

    @NotNull
    private String study;

    @NotNull
    private String speak;

    @NotNull
    private String etc;

    public Feedback toEntity(final Long fromId, final Levellog levellog) {
        return new Feedback(fromId, levellog, study, speak, etc);
    }

    public static FeedbackContentRequest from(final Feedback feedback) {
        return new FeedbackContentRequest(feedback.getStudy(), feedback.getSpeak(), feedback.getEtc());
    }

    public Feedback toFeedback(final Member member, final Levellog levellog) {
        return new Feedback(member, levellog.getAuthor(), levellog, study, speak, etc);
    }
}
