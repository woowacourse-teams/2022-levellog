package com.woowacourse.levellog.support;

import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.support.DebugMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DebugMessage의")
class DebugMessageTest {

    @Test
    @DisplayName("build 메서드는 디버그용 필드를 문자열 형식으로 만들어 준다.")
    void build() {
        // given
        final DebugMessage debugMessage = DebugMessage.init()
                .append("levellogId", 1L)
                .append("memberId", 3L)
                .append("startAt", TEAM_START_TIME);

        // when
        final String result = debugMessage.build();

        // then
        assertThat(result).isEqualTo(" [levellogId: 1, memberId: 3, startAt: " + TEAM_START_TIME + "]");
    }
}
