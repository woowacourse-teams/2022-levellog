package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
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

    public Feedback toEntity(final Long fromId, final Levellog levellog) {
        return new Feedback(fromId, levellog, feedback.getStudy(), feedback.getSpeak(),
                feedback.getEtc());
    }
}
