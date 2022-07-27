package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("LevellogTest 의")
class LevellogTest {

    @Nested
    @DisplayName("생성자는")
    class constructor {

        @Test
        @DisplayName("레벨로그 작성자가 null인 경우 예외를 던진다.")
        void newLevellog_authorNull_Exception() {
            // given
            final Team team = new Team("잠실 제이슨조,", "트랙룸", LocalDateTime.now(), "jamsil_trackroom.png");
            final String content = "JPA에 대해 학습함";

            //  when & then
            assertThatThrownBy(() -> new Levellog(null, team, content))
                    .isInstanceOf(InvalidFieldException.class);
        }

        @Test
        @DisplayName("레벨로그 팀이 null인 경우 예외를 던진다.")
        void newLevellog_teamNull_Exception() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final String content = "JPA에 대해 학습함";

            //  when & then
            assertThatThrownBy(() -> new Levellog(author, null, content))
                    .isInstanceOf(InvalidFieldException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
        void levellog_contentBlank_Exception(final String invalidContent) {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Team team = new Team("잠실 제이슨조,", "트랙룸", LocalDateTime.now(), "jamsil_trackroom.png");

            //  when & then
            assertThatThrownBy(() -> new Levellog(author, team, invalidContent))
                    .isInstanceOf(InvalidFieldException.class);
        }
    }

    @Test
    @DisplayName("updateContent 메서드는 레벨로그 내용을 변경한다.")
    void updateContent() {
        // given
        final Member author = new Member("페퍼", 1111, "pepper.png");
        final Team team = new Team("잠실 제이슨조,", "트랙룸", LocalDateTime.now(), "jamsil_trackroom.png");
        final Levellog levellog = new Levellog(author, team, "JPA에 대해 학습함");

        // when
        levellog.updateContent("Java 제네릭에 대해 학습함.");

        // then
        assertThat(levellog.getContent()).isEqualTo("Java 제네릭에 대해 학습함.");
    }
}
