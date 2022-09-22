package com.woowacourse.levellog.interviewquestion.domain;

public enum InterviewQuestionSort {

    LATEST("created_at", "DESC"),
    OLDEST("created_at", "ASC"),
    LIKES("like_count", "DESC");

    private final String field;
    private final String order;

    InterviewQuestionSort(final String field, final String order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public String getOrder() {
        return order;
    }
}
