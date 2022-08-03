package com.woowacourse.levellog.team.support;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeStandard implements TimeStandard {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
