package com.woowacourse.levellog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbackResponse {

    private Long id;
    private String name;
    private FeedbackContentDto feedback;
}
