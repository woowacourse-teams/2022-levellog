package com.woowacourse.levellog.common.domain;

import java.util.Arrays;
import java.util.Set;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

public enum Emails {

    ADMIN(Set.of("ozragwort@gmail.com", "ilseongdev@gmail.com")),

    EMPTY(Set.of());

    private static final Environment environment = new StandardEnvironment();

    private final Set<String> emails;

    Emails(final Set<String> emails) {
        this.emails = emails;
    }

    public static Emails createInstance() {
        if (isProd()) {
            return ADMIN;
        }
        return EMPTY;
    }

    private static boolean isProd() {
        return Arrays.asList(environment.getActiveProfiles())
                .contains("prod");
    }

    public Set<String> get() {
        return emails;
    }
}
