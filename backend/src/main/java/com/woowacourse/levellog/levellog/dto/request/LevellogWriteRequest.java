package com.woowacourse.levellog.levellog.dto.request;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.team.domain.Team;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LevellogWriteRequest {

    @NotBlank
    private String content;

    public Levellog toLevellog(final Long authorId, final Team team) {
        return new Levellog(authorId, team, content);
    }
}
