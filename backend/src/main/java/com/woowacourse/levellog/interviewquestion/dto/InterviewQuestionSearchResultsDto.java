package com.woowacourse.levellog.interviewquestion.dto;

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
public class InterviewQuestionSearchResultsDto {

    private List<InterviewQuestionSearchResultDto> results;
    private Long page;

    public static InterviewQuestionSearchResultsDto of(final List<InterviewQuestionSearchResultDto> results,
                                                       final Long page) {
        return new InterviewQuestionSearchResultsDto(results, page);
    }
}
