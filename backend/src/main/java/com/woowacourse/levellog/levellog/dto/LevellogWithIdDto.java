package com.woowacourse.levellog.levellog.dto;

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
public class LevellogWithIdDto {

    private Long id;

    private String content;

    public static LevellogWithIdDto from(final Levellog levellog) {
        return new LevellogWithIdDto(levellog.getId(), levellog.getContent());
    }
}
