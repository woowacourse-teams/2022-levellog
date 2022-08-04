package com.woowacourse.levellog.feedback.dto;

<<<<<<< HEAD
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
=======
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
>>>>>>> main
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
<<<<<<< HEAD
@EqualsAndHashCode
=======
>>>>>>> main
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackContentDto {

    @NotNull
    private String study;

    @NotNull
    private String speak;

    @NotNull
    private String etc;
<<<<<<< HEAD

    public static FeedbackContentDto from(final Feedback feedback) {
        return new FeedbackContentDto(feedback.getStudy(), feedback.getSpeak(), feedback.getEtc());
    }

    public Feedback toFeedback(final Member member, final Levellog levellog) {
        return new Feedback(member, levellog.getAuthor(), levellog, study, speak, etc);
    }
=======
>>>>>>> main
}
