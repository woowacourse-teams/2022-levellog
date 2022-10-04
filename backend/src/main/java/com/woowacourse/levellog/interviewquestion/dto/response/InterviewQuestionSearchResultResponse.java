package com.woowacourse.levellog.interviewquestion.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionSearchResultResponse {

    private Long id;
    private String content;
    private boolean like;
    private int likeCount;
}
