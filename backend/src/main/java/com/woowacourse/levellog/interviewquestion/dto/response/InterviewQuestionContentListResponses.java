package com.woowacourse.levellog.interviewquestion.dto.response;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionContentListResponses {

    private List<InterviewQuestionContentResponse> interviewQuestions;

    public static InterviewQuestionContentListResponses from(final List<InterviewQuestion> interviewQuestions) {
        return new InterviewQuestionContentListResponses(interviewQuestions.stream()
                .map(InterviewQuestionContentResponse::of)
                .collect(Collectors.toList()));
    }
}
