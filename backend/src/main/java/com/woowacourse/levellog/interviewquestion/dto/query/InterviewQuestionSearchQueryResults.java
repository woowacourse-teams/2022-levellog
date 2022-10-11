package com.woowacourse.levellog.interviewquestion.dto.query;

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
public class InterviewQuestionSearchQueryResults {

    private List<InterviewQuestionSearchQueryResult> results;
    private Long page;
}
