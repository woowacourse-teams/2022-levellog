package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.member.domain.Member;
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

    private List<InterviewQuestionDto> interviewQuestions;

    public static InterviewQuestionsDto from(final List<InterviewQuestion> interviewQuestions) {
        final List<InterviewQuestionDto> responses = interviewQuestions.stream()
                .map(InterviewQuestion::getAuthor)
                .distinct()
                .map(it -> InterviewQuestionDto.of(it, toContents(interviewQuestions, it)))
                .collect(Collectors.toList());

        return new InterviewQuestionsDto(responses);
    }

    private static List<InterviewQuestionContentDto> toContents(final List<InterviewQuestion> interviewQuestions,
                                                                final Member author) {
        return interviewQuestions.stream()
                .filter(it -> it.isAuthor(author))
                .map(InterviewQuestionContentDto::of)
                .collect(Collectors.toList());
    }
}
