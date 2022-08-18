package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.team.domain.TeamStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("TeamStatus의")
class TeamStatusTest {

    @ParameterizedTest
    @CsvSource(value = {"ready, false", "in-progress, false", "closed, true"})
    @DisplayName("isClosed 메서드는 입력 받은 status 값에 따라 종료 여부를 반환한다.")
    void isClosed(final String status, final boolean expected) {
        // when
        final boolean actual = TeamStatus.isClosed(status);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}