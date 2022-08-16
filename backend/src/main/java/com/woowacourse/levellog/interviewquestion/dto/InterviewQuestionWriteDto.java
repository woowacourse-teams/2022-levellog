package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
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
    private String interviewQuestion;

    public static InterviewQuestionWriteDto from(final String interviewQuestion) {
        return new InterviewQuestionWriteDto(interviewQuestion);
    }

    public InterviewQuestion toInterviewQuestion(final Member fromMember, final Levellog levellog) {
        return InterviewQuestion.of(fromMember, levellog, interviewQuestion);
    }
}
