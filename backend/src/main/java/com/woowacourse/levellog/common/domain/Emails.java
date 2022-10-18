package com.woowacourse.levellog.common.domain;

import com.woowacourse.levellog.common.support.EnvironmentFactory;
import java.util.Arrays;
import java.util.Set;
import org.springframework.core.env.Environment;

public enum Emails {

    ADMIN(Set.of("ozragwort@gmail.com", "ilseongdev@gmail.com")),

    EMPTY(Set.of());

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
        final Environment environment = EnvironmentFactory.get();
        return Arrays.asList(environment.getActiveProfiles())
                .contains("prod");
    }

    public Set<String> get() {
        return emails;
    }
}
