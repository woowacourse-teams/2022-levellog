package com.woowacourse.levellog.prequestion.dto;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreQuestionDto {

    private MemberDto author;
    private String content;

    public static PreQuestionDto from(final Member author, final String content) {
        return new PreQuestionDto(MemberDto.from(author), content);
    }
}
