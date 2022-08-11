package com.woowacourse.levellog.config;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.BEFORE_START_TIME;

import com.woowacourse.levellog.team.support.TimeStandard;
import java.time.LocalDateTime;

public class FakeTimeStandard implements TimeStandard {

    private LocalDateTime now = BEFORE_START_TIME;

    @Override
    public LocalDateTime now() {
        return now;
    }

    public void setBeforeStarted() {
        now = BEFORE_START_TIME;
    }

    public void setInProgress() {
        now = AFTER_START_TIME;
    }
}
