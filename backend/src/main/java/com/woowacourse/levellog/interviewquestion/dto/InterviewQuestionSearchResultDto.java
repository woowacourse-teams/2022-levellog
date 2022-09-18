package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionSearchResultDto {

    private Long id;
    private String content;
    private boolean like;
    private int likeCount;

    public static InterviewQuestionSearchResultDto of(final InterviewQuestion interviewQuestion, final boolean like) {
        return new InterviewQuestionSearchResultDto(
                interviewQuestion.getId(), interviewQuestion.getContent(), like, interviewQuestion.getLikes());
    }
}
