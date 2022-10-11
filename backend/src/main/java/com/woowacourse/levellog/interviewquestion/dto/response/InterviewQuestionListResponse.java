package com.woowacourse.levellog.interviewquestion.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionListResponse {

    private List<InterviewQuestionResponse> interviewQuestions;

    public static InterviewQuestionListResponse from(final List<InterviewQuestion> interviewQuestions) {
        final List<InterviewQuestionResponse> responses = interviewQuestions.stream()
                .map(InterviewQuestion::getAuthor)
                .distinct()
                .map(it -> InterviewQuestionResponse.of(it, toContents(interviewQuestions, it)))
                .collect(Collectors.toList());

        return new InterviewQuestionListResponse(responses);
    }

    private static List<InterviewQuestionContentResponse> toContents(final List<InterviewQuestion> interviewQuestions,
                                                                     final Member author) {
        return interviewQuestions.stream()
                .filter(it -> it.isAuthor(author))
                .map(InterviewQuestionContentResponse::of)
                .collect(Collectors.toList());
    }
}
