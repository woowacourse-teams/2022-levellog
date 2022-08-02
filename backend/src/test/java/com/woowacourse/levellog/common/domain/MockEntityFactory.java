package com.woowacourse.levellog.common.domain;

public class MockEntityFactory extends BaseEntity {

    public static <T extends BaseEntity> T setId(final Long id, final T entity) {
        entity.id = id;
        return entity;
    }
}
