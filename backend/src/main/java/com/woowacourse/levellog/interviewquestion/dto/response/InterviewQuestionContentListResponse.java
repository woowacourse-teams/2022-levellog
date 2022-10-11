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
public class InterviewQuestionContentListResponse {

    private List<InterviewQuestionContentResponse> interviewQuestions;

    public static InterviewQuestionContentListResponse from(final List<InterviewQuestion> interviewQuestions) {
        return new InterviewQuestionContentListResponse(interviewQuestions.stream()
                .map(InterviewQuestionContentResponse::of)
                .collect(Collectors.toList()));
    }
}