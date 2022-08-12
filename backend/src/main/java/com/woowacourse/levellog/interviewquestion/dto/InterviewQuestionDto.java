package com.woowacourse.levellog.interviewquestion.dto;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewQuestionDto {

    private MemberDto author;
    private List<InterviewQuestionContentDto> contents;

    public static InterviewQuestionDto of(final Member author, final List<InterviewQuestionContentDto> contents) {
        return new InterviewQuestionDto(MemberDto.from(author), contents);
    }
}
