package com.woowacourse.levellog.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackContentDto {

    @NotNull
    private String study;

    @NotNull
    private String speak;

    @NotNull
    private String etc;
}
