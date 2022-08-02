package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionsDto {

    private List<InterviewQuestionDetailDto> interviewQuestions;

    public static InterviewQuestionsDto from(final List<InterviewQuestion> interviewQuestions) {
        return new InterviewQuestionsDto(interviewQuestions.stream()
                .map(InterviewQuestionDetailDto::of)
                .collect(Collectors.toList()));
    }
}
