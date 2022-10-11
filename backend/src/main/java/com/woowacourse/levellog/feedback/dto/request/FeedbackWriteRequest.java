package com.woowacourse.levellog.feedback.dto.request;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackWriteRequest {

    @NotNull
    @Valid
    private FeedbackContentRequest feedback;

    public FeedbackWriteRequest(final String study, final String speak, final String etc) {
        this.feedback = new FeedbackContentRequest(study, speak, etc);
    }

    public Feedback toEntity(final Long fromId, final Levellog levellog) {
        return new Feedback(fromId, levellog, feedback.getStudy(), feedback.getSpeak(),
                feedback.getEtc());
    }
}
