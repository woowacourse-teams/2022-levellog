package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SimpleInterviewQuestionDto {

    private MemberDto author;
    private InterviewQuestionContentDto content;
}
