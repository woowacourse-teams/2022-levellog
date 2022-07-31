package com.woowacourse.levellog.interview_question.dto;

import com.woowacourse.levellog.interview_question.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionDto {

    @NotNull
    private String content;

    public InterviewQuestion toInterviewQuestion(final Member fromMember, final Levellog levellog) {
        return InterviewQuestion.of(fromMember, levellog.getAuthor(), levellog, content);
    }
}
