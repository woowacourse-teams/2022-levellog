package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("LevellogRepository의")
class LevellogRepositoryTest {

    @Autowired
    private LevellogRepository levellogRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("findByAuthorIdAndTeamId 메서드는 memberId와 teamId이 모두 일치하는 레벨로그를 반환한다.")
    void findByAuthorIdAndTeamId() {
        // given
        final Member author = memberRepository.save(new Member("pepper", 1111, "pepper.png"));
        final Team team = teamRepository.save(
                new Team("선릉 브라운조", "무중력 광장", LocalDateTime.now().plusDays(1), "campus.png", 1));
        final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "Spring을 학습하였습니다."));

        final Long authorId = author.getId();
        final Long teamId = team.getId();

        // when
        final Optional<Levellog> actual = levellogRepository.findByAuthorIdAndTeamId(authorId, teamId);

        // then
        assertThat(actual).hasValue(levellog);
    }

    @Nested
    @DisplayName("existsByAuthorIdAndTeamId 메서드는")
    class ExistsByAuthorIdAndTeamIdTest {

        @Test
        @DisplayName("memberId와 teamId이 모두 일치하는 레벨로그가 존재하는 경우 true를 반환한다.")
        void exists() {
            // given
            final Member author = memberRepository.save(new Member("pepper", 1111, "pepper.png"));
            final Team team = teamRepository.save(
                    new Team("선릉 브라운조", "무중력 광장", LocalDateTime.now().plusDays(1), "campus.png", 1));
            levellogRepository.save(Levellog.of(author, team, "Spring을 학습하였습니다."));

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
            final Member author = memberRepository.save(new Member("pepper", 1111, "pepper.png"));
            final Team team = teamRepository.save(
                    new Team("선릉 브라운조", "무중력 광장", LocalDateTime.now().plusDays(1), "campus.png", 1));
            levellogRepository.save(Levellog.of(author, team, "Spring을 학습하였습니다."));

            final Long anotherAuthorId = author.getId() + 1;
            final Long teamId = team.getId();

            // when
            final boolean actual = levellogRepository.existsByAuthorIdAndTeamId(anotherAuthorId, teamId);

            // then
            assertFalse(actual);
        }
    }

    @Test
    @DisplayName("findAllByAuthor 메서드는 주어진 author가 작성한 레벨로그를 모두 반환한다.")
    void findAllByAuthor() {
        // given
        final Member author = memberRepository.save(new Member("pepper", 1111, "pepper.png"));
        final Member anotherAuthor = memberRepository.save(new Member("roma", 12345, "roma.png"));
        final Team team = teamRepository.save(
                new Team("선릉 브라운조", "무중력 광장", LocalDateTime.now().plusDays(1), "campus.png", 1));
        final Team team2 = teamRepository.save(
                new Team("선릉 브라운조", "무중력 광장", LocalDateTime.now().plusDays(1), "campus.png", 1));

        final Levellog authorLevellog1 = levellogRepository.save(Levellog.of(author, team, "Spring을 학습하였습니다."));
        final Levellog authorLevellog2 = levellogRepository.save(Levellog.of(author, team2, "JPA를 학습하였습니다."));
        final Levellog anotherLevelog = levellogRepository.save(Levellog.of(anotherAuthor, team, "리액트를 학습하였습니다."));

        // when
        final List<Levellog> levellogs = levellogRepository.findAllByAuthor(author);

        // then
        assertAll(
                () -> assertThat(levellogs).hasSize(2),
                () -> assertThat(levellogs).contains(authorLevellog1, authorLevellog2)
        );
    }
}
