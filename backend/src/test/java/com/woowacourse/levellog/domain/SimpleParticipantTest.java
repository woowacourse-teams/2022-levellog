package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.team.domain.SimpleParticipant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("SimpleParticipant의")
class SimpleParticipantTest {

    @ParameterizedTest
    @DisplayName("isParticipant 메서드는")
    @CsvSource(value = {"true,false", "false,true"})
    void isParticipant(final boolean isWatcher, final boolean expected) {
        // given
        final SimpleParticipant simpleParticipant = new SimpleParticipant(1L, 1L, false, isWatcher);

        // when
        final boolean actual = simpleParticipant.isParticipant();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
