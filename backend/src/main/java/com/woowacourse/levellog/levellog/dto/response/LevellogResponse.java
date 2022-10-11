package com.woowacourse.levellog.levellog.dto.response;

import com.woowacourse.levellog.levellog.domain.Levellog;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LevellogResponse {

    private Long id;

    private String content;

    public static LevellogResponse from(final Levellog levellog) {
        return new LevellogResponse(levellog.getId(), levellog.getContent());
    }
}
