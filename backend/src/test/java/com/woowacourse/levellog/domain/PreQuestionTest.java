package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PreQuestion의")
public class PreQuestionTest {

    final Member author = new Member("알린", 12345678, "알린.img");
    final Member from = new Member("로마", 56781234, "로마.img");

    @BeforeEach
    void setUp() {
        MockEntityFactory.setId(1L, author);
        MockEntityFactory.setId(2L, from);
    }

    @Nested
    @DisplayName("생성자 메서드는")
    class Constructor {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문이 null 또는 공백이 들어오면 예외를 던진다.")
        void constructor_preQuestionNullOrBlank_exception(final String preQuestion) {
            // given
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "알린의 레벨로그");

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, from.getId(), preQuestion))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("사전 내용은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("내가 쓴 레벨로그의 사전 질문을 작성하면 예외를 던진다.")
        void constructor_preQuestionMyLevellog_exception() {
            // given
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "알린의 레벨로그");

            final String preQuestion = "알린의 사전 질문";

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, author.getId(), preQuestion))
                    .isInstanceOf(InvalidPreQuestionException.class)
                    .hasMessageContaining("잘못된 사전 질문 요청입니다.");
        }

    }
    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("사전 질문을 수정한다.")
        void success() {
            // given
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "알린의 레벨로그");

            final PreQuestion preQuestion = new PreQuestion(levellog, from.getId(), "로마가 쓴 사전 질문");

            // when
            preQuestion.update("수정된 사전 질문");

            // then
            assertThat(preQuestion.getContent()).isEqualTo("수정된 사전 질문");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문이 null 또는 공백이 들어오면 예외를 던진다.")
        void update_preQuestionNullOrBlank_exception(final String preQuestion) {
            // given
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(author.getId(), team, "알린의 레벨로그");

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, from.getId(), preQuestion))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("사전 내용은 공백이나 null일 수 없습니다.");
        }
    }
}
