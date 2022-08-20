package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DisplayName("TeamRepository의")
class TeamRepositoryTest extends RepositoryTest {

    private Team saveTeam(final String title) {
        return teamRepository.save(new Team(title, "트랙룸", TimeFixture.TEAM_START_TIME, "jamsil.img", 1));
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("입력 받은 Sort 조건에 따라 정렬된 팀 List를 반환한다.")
        void findAll_sorting_success() {
            // given
            final Team team1 = saveTeam("잠실 네오조");
            final Team team2 = saveTeam("잠실 제이슨조");
            final Team team3 = saveTeam("잠실 토미조");
            final Team team4 = saveTeam("잠실 브리조");

            team3.close(TimeFixture.AFTER_START_TIME);
            team4.close(TimeFixture.AFTER_START_TIME);

            final Sort sort = Sort.by(
                    Sort.Order.asc("isClosed"),
                    Sort.Order.desc("createdAt")
            );
            final Pageable pageable = PageRequest.of(0, 4, sort);

            // when
            final Page<Team> teamResult = teamRepository.findAll(pageable);

            // then
            final List<Team> teams = teamResult.getContent();
            assertThat(teams).containsExactly(team2, team1, team4, team3);
        }

        @Test
        @DisplayName("입력 받은 페이징 조건에 따라 페이징 된 팀 List를 반환한다.")
        void findAll_paging_success() {
            // given
            saveTeam("잠실 네오조");
            saveTeam("잠실 제이슨조");
            saveTeam("잠실 토미조");
            saveTeam("잠실 브리조");

            final Sort sort = Sort.by(
                    Sort.Order.asc("isClosed"),
                    Sort.Order.desc("createdAt")
            );
            final Pageable pageable = PageRequest.of(1, 2, sort);

            // when
            final Page<Team> teamResult = teamRepository.findAll(pageable);

            // then
            final List<Team> teams = teamResult.getContent();
            Assertions.assertAll(() -> {
                assertThat(teams.size()).isEqualTo(2);
                assertThat(teamResult.getNumber()).isEqualTo(1); // 현재 페이지 번호
                assertThat(teamResult.getTotalElements()).isEqualTo(4); // 페이징 되지 않은 데이터 포함 전체 데이터 개수
                assertThat(teamResult.getTotalPages()).isEqualTo(2); // 총 페이지 개수
            });
        }
    }

    @Test
    @DisplayName("findAllByIsClosed 메소드는 입력 받은 팀 종료 여부와 페이징 조건에 따라 필터링, 페이징 된 List를 반환한다.")
    void findAllByIsClosed() {
        // given
        final Team team1 = saveTeam("잠실 네오조");
        final Team team2 = saveTeam("잠실 제이슨조");
        final Team team3 = saveTeam("잠실 토미조");
        final Team team4 = saveTeam("잠실 브리조");

        team3.close(TimeFixture.AFTER_START_TIME);
        team4.close(TimeFixture.AFTER_START_TIME);

        final Pageable pageable = PageRequest.of(0, 4, Sort.by(Direction.DESC, "createdAt"));

        // when
        final Page<Team> teamResult = teamRepository.findAllByIsClosed(true, pageable);

        // then
        final List<Team> closedTeams = teamResult.getContent();
        Assertions.assertAll(() -> {
            assertThat(closedTeams).containsExactly(team4, team3);
            assertThat(teamResult.getNumber()).isZero();
            assertThat(teamResult.getTotalElements()).isEqualTo(2);
        });
    }
}
