package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LevellogRepository의")
class LevellogRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByAuthorIdAndTeamId 메서드는 memberId와 teamId이 모두 일치하는 레벨로그를 반환한다.")
    void findByAuthorIdAndTeamId() {
        // given
        final Member author = getMember("pepper");
        final Team team = getTeam(author);
        final Levellog levellog = getLevellog(author, team);

        final Long authorId = author.getId();
        final Long teamId = team.getId();

        // when
        final Optional<Levellog> actual = levellogRepository.findByAuthorIdAndTeamId(authorId, teamId);

        // then
        assertThat(actual).hasValue(levellog);
    }

    @Test
    @DisplayName("findAllByAuthor 메서드는 주어진 author가 작성한 레벨로그를 모두 반환한다.")
    void findAllByAuthor() {
        // given
        final Member author = getMember("pepper");
        final Member anotherAuthor = getMember("roma");

        final Team team = getTeam(author);
        final Team team2 = getTeam(anotherAuthor, author);

        final Levellog authorLevellog1 = getLevellog(author, team);
        final Levellog authorLevellog2 = getLevellog(author, team2);
        getLevellog(anotherAuthor, team);

        // when
        final List<Levellog> levellogs = levellogRepository.findAllByAuthor(author);

        // then
        assertAll(
                () -> assertThat(levellogs).hasSize(2),
                () -> assertThat(levellogs).contains(authorLevellog1, authorLevellog2)
        );
    }

    @Nested
    @DisplayName("existsByAuthorIdAndTeamId 메서드는")
    class ExistsByAuthorIdAndTeamIdTest {

        @Test
        @DisplayName("memberId와 teamId이 모두 일치하는 레벨로그가 존재하는 경우 true를 반환한다.")
        void exists() {
            // given
            final Member author = getMember("pepper");
            final Team team = getTeam(author);
            getLevellog(author, team);

            final Long authorId = author.getId();
            final Long teamId = team.getId();

            // when
            final boolean actual = levellogRepository.existsByAuthorIdAndTeamId(authorId, teamId);

            // then
            assertTrue(actual);
        }

        @Test
        @DisplayName("memberId와 teamId이 모두 일치하는 레벨로그가 존재하지 않는 경우 false를 반환한다.")
        void notExists() {
            // given
            final Member author = getMember("pepper");
            final Team team = getTeam(author);
            getLevellog(author, team);

            final Long anotherAuthorId = author.getId() + 1;
            final Long teamId = team.getId();

            // when
            final boolean actual = levellogRepository.existsByAuthorIdAndTeamId(anotherAuthorId, teamId);

            // then
            assertFalse(actual);
        }
    }
}
