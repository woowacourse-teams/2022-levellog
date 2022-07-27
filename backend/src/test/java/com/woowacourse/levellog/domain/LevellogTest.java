package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("LevellogTest 의")
class LevellogTest {

    @ParameterizedTest
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    @DisplayName("생성자는 레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
    void newLevellog_contentBlank_Exception(final String invalidContent) {
        // given
        final Member author = new Member("페퍼", 1111, "pepper.png");
        final Team team = new Team("잠실 제이슨조,", "트랙룸", LocalDateTime.now(), "jamsil_trackroom.png");

        //  when & then
        assertThatThrownBy(() -> new Levellog(author, team, invalidContent))
                .isInstanceOf(InvalidFieldException.class);
    }
}
