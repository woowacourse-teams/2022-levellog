package com.woowacourse.levellog.fixture;

@FunctionalInterface
public interface RequestExecutor {

    RestAssuredResponse execute();
}
