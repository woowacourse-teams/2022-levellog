package com.woowacourse.levellog.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackCreateRequest {

    private String name;
    private FeedbackContentDto feedback;
}
