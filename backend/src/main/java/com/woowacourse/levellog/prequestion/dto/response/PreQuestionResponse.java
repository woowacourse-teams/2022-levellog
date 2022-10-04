package com.woowacourse.levellog.prequestion.dto.response;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreQuestionResponse {

    private MemberResponse author;
    private String content;

    public static PreQuestionResponse from(final Member author, final String content) {
        return new PreQuestionResponse(MemberResponse.from(author), content);
    }
}
