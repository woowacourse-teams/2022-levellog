package com.woowacourse.levellog.common.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DebugMessage {

    private final Map<String, Object> fields;

    private DebugMessage() {
        this.fields = new LinkedHashMap<>();
    }

    public static DebugMessage init() {
        return new DebugMessage();
    }

    public DebugMessage append(final String key, final Object value) {
        fields.put(key, value);
        return this;
    }

    public String build() {
        return fields.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", ", " [", "]"));
    }
}
