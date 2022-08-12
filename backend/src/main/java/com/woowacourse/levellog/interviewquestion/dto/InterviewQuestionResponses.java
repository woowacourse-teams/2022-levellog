package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.ArrayList;
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
public class InterviewQuestionResponses {

    private List<InterviewQuestionResponse> interviewQuestions;

    public static InterviewQuestionResponses from(final List<InterviewQuestion> interviewQuestions) {
        final List<Member> authors = interviewQuestions.stream()
                .map(InterviewQuestion::getAuthor)
                .distinct()
                .collect(Collectors.toList());

        final List<InterviewQuestionResponse> responses = new ArrayList<>();
        for (final Member author : authors) {
            final List<InterviewQuestionDetailDto> contents = interviewQuestions.stream()
                    .filter(it -> it.getAuthor().equals(author))
                    .map(InterviewQuestionDetailDto::of)
                    .collect(Collectors.toList());

            final InterviewQuestionResponse response = new InterviewQuestionResponse(MemberDto.from(author), contents);
            responses.add(response);
        }

        return new InterviewQuestionResponses(responses);
    }
}
