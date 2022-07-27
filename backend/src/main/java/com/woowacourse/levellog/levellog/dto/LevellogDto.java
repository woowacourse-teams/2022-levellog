package com.woowacourse.levellog.levellog.dto;

import com.woowacourse.levellog.levellog.domain.Levellog;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LevellogDto {

    private final String content;

    public static LevellogDto from(final Levellog levellog) {
        return new LevellogDto(levellog.getContent());
    }
}
