package com.woowacourse.levellog.common.domain;

/**
 * Entity ID(PK)에 의존하는 테스트를 쉽게 하기 위해서 ID가 null인 entity에 ID를 할당합니다.
 * @see com.woowacourse.levellog.team.domain.ParticipantsTest#getMember
 */
public class MockEntityFactory {

    public static <T extends BaseEntity> T setId(final Long id, final T entity) {
        entity.id = id;
        return entity;
    }
}
