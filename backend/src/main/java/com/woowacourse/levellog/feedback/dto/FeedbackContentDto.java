package com.woowacourse.levellog.feedback.dto;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
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
public class FeedbackContentDto {

    @NotNull
    private String study;

    @NotNull
    private String speak;

    @NotNull
    private String etc;

    public static FeedbackContentDto from(final Feedback feedback) {
        return new FeedbackContentDto(feedback.getStudy(), feedback.getSpeak(), feedback.getEtc());
    }

    public Feedback toEntity(final Member member, final Levellog levellog) {
        return new Feedback(member, levellog.getAuthor(), levellog, study, speak, etc);
    }
}
