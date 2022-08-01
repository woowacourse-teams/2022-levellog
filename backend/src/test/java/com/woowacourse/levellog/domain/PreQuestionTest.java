package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("PreQuestion의")
public class PreQuestionTest {

    @Nested
    @DisplayName("생성자 메서드는")
    class constructor {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문이 null 또는 공백이 들어오면 예외를 던진다.")
        void preQuestionNullOrBlank_Exception(final String preQuestion) {
            // given
            final Member author = new Member("알린", 12345678, "알린.img");
            final Team team = new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img");
            final Levellog levellog = Levellog.of(author, team, "알린의 레벨로그");

            final Member from = new Member("로마", 56781234, "로마.img");

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, from, preQuestion))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("사전 내용은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("내가 쓴 레벨로그의 사전 질문을 작성하면 예외를 던진다.")
        void preQuestionMyLevellog_Exception() {
            // given
            final Member author = new Member("알린", 12345678, "알린.img");
            final Team team = new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img");
            final Levellog levellog = Levellog.of(author, team, "알린의 레벨로그");

            final String preQuestion = "알린의 사전 질문";

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, author, preQuestion))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("내 레벨로그에 사전 질문을 작성할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("사전 질문을 수정한다.")
        void update() {
            // given
            final Member author = new Member("알린", 12345678, "알린.img");
            final Team team = new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img");
            final Levellog levellog = Levellog.of(author, team, "알린의 레벨로그");

            final Member from = new Member("로마", 56781234, "로마.img");

            final PreQuestion preQuestion = new PreQuestion(levellog, from, "로마가 쓴 사전 질문");

            // when
            preQuestion.update("수정된 사전 질문");

            // then
            assertThat(preQuestion.getPreQuestion()).isEqualTo("수정된 사전 질문");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @DisplayName("사전 질문이 null 또는 공백이 들어오면 예외를 던진다.")
        void preQuestionNullOrBlank_Exception(final String preQuestion) {
            // given
            final Member author = new Member("알린", 12345678, "알린.img");
            final Team team = new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img");
            final Levellog levellog = Levellog.of(author, team, "알린의 레벨로그");

            final Member from = new Member("로마", 56781234, "로마.img");

            // when & then
            assertThatThrownBy(() -> new PreQuestion(levellog, from, preQuestion))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("사전 내용은 공백이나 null일 수 없습니다.");
        }
    }
}
