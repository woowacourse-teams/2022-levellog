package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.team.domain.Team;
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
    class Constructor {

        @Test
        @DisplayName("레벨로그를 생성한다.")
        void success() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Team team = TeamTest.saveTeam();
            final String content = "Spring을 학습하였습니다";

            // when & then
            assertDoesNotThrow(() -> new Levellog(author.getId(), team, content));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
        void newLevellog_contentBlank_exception(final String invalidContent) {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Team team = TeamTest.saveTeam();

            //  when & then
            assertThatThrownBy(() -> new Levellog(author.getId(), team, invalidContent))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("updateContent 메서드는")
    class UpdateContent {

        @Test
        @DisplayName("레벨로그 내용을 업데이트한다.")
        void success() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            MockEntityFactory.setId(1L, author);
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "content");
            final String updatedContent = "updated content";

            // when
            levellog.updateContent(author.getId(), updatedContent);

            // then
            assertThat(levellog.getContent()).isEqualTo(updatedContent);
        }

        @Test
        @DisplayName("작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void updateContent_unauthorized_exception() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member member = new Member("알린", 2222, "alien.png");
            MockEntityFactory.setId(1L, author);
            MockEntityFactory.setId(2L, member);
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "content");

            //  when & then
            assertThatThrownBy(() -> levellog.updateContent(member.getId(), "update content"))
                    .isInstanceOf(MemberNotAuthorException.class)
                    .hasMessageContainingAll("작성자가 아닙니다.", String.valueOf(member.getId()),
                            String.valueOf(levellog.getId()));

        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
        void updateContent_contentBlank_exception(final String invalidContent) {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            MockEntityFactory.setId(1L, author);
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "content");

            //  when & then
            assertThatThrownBy(() -> levellog.updateContent(author.getId(), invalidContent))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }
}
