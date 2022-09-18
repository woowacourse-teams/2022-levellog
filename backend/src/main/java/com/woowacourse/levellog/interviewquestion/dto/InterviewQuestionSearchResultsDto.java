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

    public static InterviewQuestionSearchResultsDto of(final List<InterviewQuestionSearchResultDto> results) {
        return new InterviewQuestionSearchResultsDto(results);
    }
}
