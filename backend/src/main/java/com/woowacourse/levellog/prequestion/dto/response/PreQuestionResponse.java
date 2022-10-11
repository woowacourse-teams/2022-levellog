package com.woowacourse.levellog.prequestion.dto.response;

import com.woowacourse.levellog.member.dto.response.MemberResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreQuestionResponse {

    private MemberResponse author;
    private String content;
}
