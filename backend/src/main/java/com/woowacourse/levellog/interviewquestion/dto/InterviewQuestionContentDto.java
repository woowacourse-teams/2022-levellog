package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionContentDto {

    private Long id;
    private String content;

    public static InterviewQuestionContentDto of(final InterviewQuestion interviewQuestion) {
        return new InterviewQuestionContentDto(interviewQuestion.getId(), interviewQuestion.getContent());
    }
}
