package com.woowacourse.levellog.interviewquestion.dto.query;

import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponse;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class InterviewQuestionQueryResult {

    private MemberResponse author;
    private InterviewQuestionContentResponse content;
}
