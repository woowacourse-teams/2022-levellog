package com.woowacourse.levellog.levellog.dto;

import com.woowacourse.levellog.levellog.domain.Levellog;
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
public class LevellogDto {

    private MemberDto author;
    private String content;

    public static LevellogDto from(final Levellog levellog) {
        return new LevellogDto(MemberDto.from(levellog.getAuthor()), levellog.getContent());
    }
}
