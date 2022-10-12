package com.woowacourse.levellog.interviewquestion.dto.response;

import com.woowacourse.levellog.member.dto.response.MemberResponse;
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

    private MemberResponse author;
    private List<InterviewQuestionContentResponse> contents;
}
