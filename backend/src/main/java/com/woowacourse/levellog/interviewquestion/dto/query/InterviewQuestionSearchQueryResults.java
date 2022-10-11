package com.woowacourse.levellog.interviewquestion.dto.query;

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
public class InterviewQuestionSearchQueryResults {

    private List<InterviewQuestionSearchQueryResult> results;
    private Long page;

    public static InterviewQuestionSearchQueryResults of(final List<InterviewQuestionSearchQueryResult> results,
                                                         final Long page) {
        return new InterviewQuestionSearchQueryResults(results, page);
    }
}
