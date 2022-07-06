package com.woowacourse.levellog.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackContentDto {

    private String study;
    private String speak;
    private String etc;
}
