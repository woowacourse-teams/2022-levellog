package com.woowacourse.levellog.common.support;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

public class EnvironmentFactory {

    private static final Environment ENVIRONMENT = new StandardEnvironment();

    public static Environment get() {
        return ENVIRONMENT;
    }
}
