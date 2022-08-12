package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionResponse {

    private MemberDto author;
    private List<InterviewQuestionDetailDto> contents;
}
