package com.woowacourse.levellog.config;

import com.woowacourse.levellog.team.support.TimeStandard;
import java.time.LocalDateTime;

public class FakeTimeStandard implements TimeStandard {

    public static final int PLUS_DAYS = 5;

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now().plusDays(PLUS_DAYS);
    }
}
