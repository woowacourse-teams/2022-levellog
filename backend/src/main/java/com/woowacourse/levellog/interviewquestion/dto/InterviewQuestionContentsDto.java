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
public class InterviewQuestionContentsDto {

    private List<InterviewQuestionContentDto> interviewQuestions;

    public static InterviewQuestionContentsDto from(final List<InterviewQuestion> interviewQuestions) {
        return new InterviewQuestionContentsDto(interviewQuestions.stream()
                .map(InterviewQuestionContentDto::of)
                .collect(Collectors.toList()));
    }
}
