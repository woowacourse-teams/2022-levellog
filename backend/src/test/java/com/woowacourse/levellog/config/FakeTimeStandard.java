package com.woowacourse.levellog.config;

import com.woowacourse.levellog.team.support.TimeStandard;
import java.time.LocalDateTime;

public class FakeTimeStandard implements TimeStandard {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now().plusDays(5);
    }
}
