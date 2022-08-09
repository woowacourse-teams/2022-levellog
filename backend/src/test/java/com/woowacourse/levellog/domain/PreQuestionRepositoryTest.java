package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PreQuestionRepository의")
class PreQuestionRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByIdAndAuthor 메서드는 preQuestionId와 From 멤버가 같은 사전 질문을 반환한다.")
    void findByIdAndAuthor() {
        // given
        final Member author = getMember("알린");
        final Member questioner = getMember("로마");
        final Team team = getTeam(author, questioner);
        final Levellog levellog = getLevellog(author, team);

        final PreQuestion preQuestion = getPreQuestion(levellog, questioner);

        // when
        final Optional<PreQuestion> actual = preQuestionRepository.findByIdAndAuthor(preQuestion.getId(), questioner);

        // then
        assertThat(actual).hasValue(preQuestion);
    }

    @Test
    @DisplayName("findByLevellogAndAuthor 메서드는 Levellog와 Author가 같은 사전 질문을 반환한다.")
    void findByLevellogAndAuthor() {
        // given
        final Member levellogAuthor = getMember("알린");
        final Member questioner = getMember("로마");
        final Team team = getTeam(levellogAuthor, questioner);
        final Levellog levellog = getLevellog(levellogAuthor, team);

        final PreQuestion preQuestion = getPreQuestion(levellog, questioner);

        // when
        final Optional<PreQuestion> actual = preQuestionRepository.findByLevellogAndAuthor(levellog, questioner);

        // then
        assertThat(actual).hasValue(preQuestion);
    }

    @Test
    @DisplayName("findByLevellogAndAuthorId 메서드는 Levellog와 AuthorId가 같은 사전 질문을 반환한다.")
    void findByLevellogAndAuthorId() {
        // given
        final Member levellogAuthor = getMember("알린");
        final Member questioner = getMember("로마");
        final Team team = getTeam(levellogAuthor, questioner);
        final Levellog levellog = getLevellog(levellogAuthor, team);

        final PreQuestion preQuestion = getPreQuestion(levellog, questioner);

        // when
        final Optional<PreQuestion> actual = preQuestionRepository.findByLevellogAndAuthorId(levellog, questioner.getId());

        // then
        assertThat(actual).hasValue(preQuestion);
    }

    @Nested
    @DisplayName("existsByLevellogAndAuthor 메서드는")
    class ExistsByLevellogAndAuthorTest {

        @Test
        @DisplayName("levellog와 사전 질문의 author가 모두 일치하는 사전 질문이 존재하는 경우 true를 반환한다.")
        void exists() {
            // given
            final Member levellogAuthor = getMember("알린");
            final Member questioner = getMember("로마");
            final Team team = getTeam(levellogAuthor, questioner);
            final Levellog levellog = getLevellog(levellogAuthor, team);

            getPreQuestion(levellog, questioner);

            // when
            final boolean actual = preQuestionRepository.existsByLevellogAndAuthor(levellog, questioner);

            // then
            assertTrue(actual);
        }

        @Test
        @DisplayName("levellog와 사전 질문의 author가 모두 일치하는 사전 질문이 존재하지 않는 경우 false를 반환한다.")
        void notExists() {
            // given
            final Member levellogAuthor = getMember("알린");
            final Member questioner = getMember("로마");
            final Team team = getTeam(levellogAuthor, questioner);
            final Levellog levellog = getLevellog(levellogAuthor, team);

            // when
            final boolean actual = preQuestionRepository.existsByLevellogAndAuthor(levellog, questioner);

            // then
            assertFalse(actual);
        }
    }
}
