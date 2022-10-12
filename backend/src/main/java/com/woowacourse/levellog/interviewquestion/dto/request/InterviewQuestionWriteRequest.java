package com.woowacourse.levellog.interviewquestion.dto.request;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionWriteRequest {

    @NotBlank
    private String content;

    public InterviewQuestion toEntity(final Long fromMemberId, final Levellog levellog) {
        return new InterviewQuestion(fromMemberId, levellog, content);
    }
}
