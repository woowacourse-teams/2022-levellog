package com.woowacourse.levellog.interviewquestion.dto.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionSearchQueryResult {

    private Long id;
    private String content;
    private boolean like;
    private int likeCount;
}
