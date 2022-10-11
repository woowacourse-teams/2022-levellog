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
public class InterviewQuestionSearchListQueryResult {

    private List<InterviewQuestionSearchQueryResult> results;
    private Long page;

    public static InterviewQuestionSearchListQueryResult of(final List<InterviewQuestionSearchQueryResult> results,
                                                            final Long page) {
        return new InterviewQuestionSearchListQueryResult(results, page);
    }
}
