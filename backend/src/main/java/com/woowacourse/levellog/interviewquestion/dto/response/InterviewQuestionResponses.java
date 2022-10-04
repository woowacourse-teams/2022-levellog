package com.woowacourse.levellog.interviewquestion.dto.response;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.member.domain.Member;
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
public class InterviewQuestionResponses {

    private List<InterviewQuestionResponse> interviewQuestions;

    public static InterviewQuestionResponses from(final List<InterviewQuestion> interviewQuestions) {
        final List<InterviewQuestionResponse> responses = interviewQuestions.stream()
                .map(InterviewQuestion::getAuthor)
                .distinct()
                .map(it -> InterviewQuestionResponse.of(it, toContents(interviewQuestions, it)))
                .collect(Collectors.toList());

        return new InterviewQuestionResponses(responses);
    }

    private static List<InterviewQuestionContentResponse> toContents(final List<InterviewQuestion> interviewQuestions,
                                                                     final Member author) {
        return interviewQuestions.stream()
                .filter(it -> it.isAuthor(author))
                .map(InterviewQuestionContentResponse::of)
                .collect(Collectors.toList());
    }
}
