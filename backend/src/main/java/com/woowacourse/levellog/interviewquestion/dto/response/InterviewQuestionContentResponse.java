package com.woowacourse.levellog.interviewquestion.dto.response;

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
public class InterviewQuestionContentResponse {

    private Long id;
    private String content;

    public static InterviewQuestionContentResponse of(final InterviewQuestion interviewQuestion) {
        return new InterviewQuestionContentResponse(interviewQuestion.getId(), interviewQuestion.getContent());
    }
}
