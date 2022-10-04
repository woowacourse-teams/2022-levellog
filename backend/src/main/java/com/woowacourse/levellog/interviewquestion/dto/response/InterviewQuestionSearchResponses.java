package com.woowacourse.levellog.interviewquestion.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionSearchResponses {

    private List<InterviewQuestionSearchResponse> results;
    private Long page;

    public static InterviewQuestionSearchResponses of(final List<InterviewQuestionSearchResponse> results,
                                                      final Long page) {
        return new InterviewQuestionSearchResponses(results, page);
    }
}
