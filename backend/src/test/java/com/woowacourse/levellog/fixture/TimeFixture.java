package com.woowacourse.levellog.fixture;

import java.time.LocalDateTime;

public class TimeFixture {

    public static final LocalDateTime BEFORE_START_TIME = LocalDateTime.now().plusDays(5);
    public static final LocalDateTime TEAM_START_TIME = LocalDateTime.now().plusDays(6);
    public static final LocalDateTime AFTER_START_TIME = LocalDateTime.now().plusDays(7);
    public static final LocalDateTime AFTER_IN_PROGRESS_START_TIME = LocalDateTime.now().plusDays(8);
}
