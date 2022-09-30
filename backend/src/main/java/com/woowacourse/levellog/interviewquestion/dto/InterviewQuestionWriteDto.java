package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionWriteDto {

    @NotBlank
    private String content;

    public static InterviewQuestionWriteDto from(final String interviewQuestion) {
        return new InterviewQuestionWriteDto(interviewQuestion);
    }

    public InterviewQuestion toInterviewQuestion(final Long fromMemberId, final Levellog levellog) {
        return InterviewQuestion.of(fromMemberId, levellog, content);
    }
}
