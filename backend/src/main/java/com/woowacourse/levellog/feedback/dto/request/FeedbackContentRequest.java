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
}
