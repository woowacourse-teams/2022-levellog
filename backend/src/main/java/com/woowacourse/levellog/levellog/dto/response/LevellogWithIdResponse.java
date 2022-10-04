package com.woowacourse.levellog.levellog.dto.response;

import com.woowacourse.levellog.levellog.domain.Levellog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LevellogWithIdResponse {

    private Long id;

    private String content;

    public static LevellogWithIdResponse from(final Levellog levellog) {
        return new LevellogWithIdResponse(levellog.getId(), levellog.getContent());
    }
}
