package com.woowacourse.levellog.levellog.dto.response;

import com.woowacourse.levellog.levellog.domain.Levellog;
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
public class LevellogResponse {

    private MemberResponse author;
    private String content;

    public static LevellogResponse from(final Levellog levellog) {
        return new LevellogResponse(MemberResponse.from(levellog.getAuthor()), levellog.getContent());
    }
}
