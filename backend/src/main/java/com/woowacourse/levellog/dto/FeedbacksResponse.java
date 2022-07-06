package com.woowacourse.levellog.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedbacksResponse {

    private List<FeedbackResponse> feedbacks;
}
