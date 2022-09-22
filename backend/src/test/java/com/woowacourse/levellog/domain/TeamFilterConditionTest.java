package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("TeamFilterCondition의")
class TeamFilterConditionTest {

    @ParameterizedTest
    @CsvSource(value = {"open, OPEN", "close, CLOSE"})
    @DisplayName("from 메서드는 입력 받은 condition 값에 따라 TeamFilterCondition 객체를 반환한다.")
    void isClosed(final String condition, final TeamFilterCondition expected) {
        // when
        final TeamFilterCondition actual = TeamFilterCondition.from(condition);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
