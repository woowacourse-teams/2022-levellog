package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PreQuestionRepository의")
class PreQuestionRepositoryTest extends RepositoryTest {

    @Nested
    @DisplayName("getPreQuestion 메서드는")
    class GetPreQuestion {

        @Test
        @DisplayName("preQuestionId에 해당하는 레코드가 존재하면 id에 해당하는 PreQuestion 엔티티를 반환한다.")
        void success() {
            // given
            final Member to = saveMember("릭");
            final Member from = saveMember("로마");
            final Team team = saveTeam(to, from);
            final Levellog levellog = saveLevellog(to, team);
            final Long expected = savePreQuestion(levellog, from)
                    .getId();

            // when
            final Long actual = preQuestionRepository.getPreQuestion(expected)
                    .getId();

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("preQuestionId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getPreQuestion_notExist_exception() {
            // given
            final Long preQuestionId = 999L;

            // when & then
            assertThatThrownBy(() -> preQuestionRepository.getPreQuestion(preQuestionId))
                    .isInstanceOf(PreQuestionNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("existsByLevellogAndAuthor 메서드는")
    class ExistsByLevellogAndAuthor {

        @Test
        @DisplayName("levellog와 사전 질문의 author가 모두 일치하는 사전 질문이 존재하는 경우 true를 반환한다.")
        void existsByLevellogAndAuthor_exists_success() {
            // given
            final Member levellogAuthor = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(levellogAuthor, questioner);
            final Levellog levellog = saveLevellog(levellogAuthor, team);

            savePreQuestion(levellog, questioner);

            // when
            final boolean actual = preQuestionRepository.existsByLevellogAndAuthorId(levellog, questioner.getId());

            // then
            assertTrue(actual);
        }

        @Test
        @DisplayName("levellog와 사전 질문의 author가 모두 일치하는 사전 질문이 존재하지 않는 경우 false를 반환한다.")
        void existsByLevellogAndAuthor_notExists_success() {
            // given
            final Member levellogAuthor = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(levellogAuthor, questioner);
            final Levellog levellog = saveLevellog(levellogAuthor, team);

            // when
            final boolean actual = preQuestionRepository.existsByLevellogAndAuthorId(levellog, questioner.getId());

            // then
            assertFalse(actual);
        }
    }
}
