package com.woowacourse.levellog.common.support;

import java.util.regex.Pattern;

public class StringConverter {

    final static Pattern INJECTION_PATTERN_CHARACTERS = Pattern.compile("['\"\\-#()@;=*/+]");

    public static String toSafeString(final String input) {
        return INJECTION_PATTERN_CHARACTERS.matcher(input)
                .replaceAll("");
    }
}
