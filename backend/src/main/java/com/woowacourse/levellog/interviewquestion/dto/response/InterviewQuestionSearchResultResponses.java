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
public class InterviewQuestionSearchResultResponses {

    private List<InterviewQuestionSearchResultResponse> results;
    private Long page;

    public static InterviewQuestionSearchResultResponses of(final List<InterviewQuestionSearchResultResponse> results,
                                                            final Long page) {
        return new InterviewQuestionSearchResultResponses(results, page);
    }
}
