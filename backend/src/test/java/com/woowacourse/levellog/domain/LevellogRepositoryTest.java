package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
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
        final Member author = saveMember("pepper");
        final Member teamMember = saveMember("roma");

        final Team team = saveTeam(author, teamMember);
        final Levellog levellog = saveLevellog(author, team);

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
        final Member author = saveMember("pepper");
        final Member authorTeamMember = saveMember("pepper");

        final Member anotherAuthor = saveMember("roma");

        final Team team = saveTeam(author, authorTeamMember);
        final Team team2 = saveTeam(anotherAuthor, author);

        final Levellog authorLevellog1 = saveLevellog(author, team);
        final Levellog authorLevellog2 = saveLevellog(author, team2);
        saveLevellog(anotherAuthor, team);

        // when
        final List<Levellog> levellogs = levellogRepository.findAllByAuthorId(author.getId());

        // then
        assertAll(
                () -> assertThat(levellogs).hasSize(2),
                () -> assertThat(levellogs).contains(authorLevellog1, authorLevellog2)
        );
    }

    @Nested
    @DisplayName("getLevellog 메서드는")
    class GetLevellog {

        @Test
        @DisplayName("levellogId에 해당하는 레코드가 존재하면 id에 해당하는 Levellog 엔티티를 반환한다.")
        void success() {
            // given
            final Member member = saveMember("릭");
            final Member teamMember = saveMember("roma");

            final Team team = saveTeam(member, teamMember);
            final Long expected = saveLevellog(member, team)
                    .getId();

            // when
            final Long actual = levellogRepository.getLevellog(expected)
                    .getId();

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("levellogId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getLevellog_notExist_exception() {
            // given
            final Long levellogId = 999L;

            // when & then
            assertThatThrownBy(() -> levellogRepository.getLevellog(levellogId))
                    .isInstanceOf(LevellogNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("existsByAuthorIdAndTeamId 메서드는")
    class ExistsByAuthorIdAndTeamId {

        @Test
        @DisplayName("memberId와 teamId이 모두 일치하는 레벨로그가 존재하는 경우 true를 반환한다.")
        void existsByAuthorIdAndTeamId_exists_success() {
            // given
            final Member author = saveMember("pepper");
            final Member teamMember = saveMember("roma");

            final Team team = saveTeam(author, teamMember);
            saveLevellog(author, team);

            final Long authorId = author.getId();
            final Long teamId = team.getId();

            // when
            final boolean actual = levellogRepository.existsByAuthorIdAndTeamId(authorId, teamId);

            // then
            assertTrue(actual);
        }

        @Test
        @DisplayName("memberId와 teamId이 모두 일치하는 레벨로그가 존재하지 않는 경우 false를 반환한다.")
        void existsByAuthorIdAndTeamId_notExists_success() {
            // given
            final Member author = saveMember("pepper");
            final Member teamMember = saveMember("roma");

            final Team team = saveTeam(author, teamMember);
            saveLevellog(author, team);

            final Long anotherAuthorId = author.getId() + 1;
            final Long teamId = team.getId();

            // when
            final boolean actual = levellogRepository.existsByAuthorIdAndTeamId(anotherAuthorId, teamId);

            // then
            assertFalse(actual);
        }
    }
}
