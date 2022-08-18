package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@DisplayName("TeamRepository의")
class TeamRepositoryTest extends RepositoryTest {

    private Team saveTeam(final String title) {
        return teamRepository.save(new Team(title, "트랙룸", TimeFixture.TEAM_START_TIME, "jamsil.img", 1));
    }

    @Test
    @DisplayName("findAll 메소드는 입력 받은 Sort 조건에 따라 정렬된 List를 반환한다.")
    void findAll() {
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

        // when
        final List<Team> teams = teamRepository.findAll(sort);

        // then
        assertThat(teams).containsExactly(team2, team1, team4, team3);
    }

    @Test
    @DisplayName("findAllByIsClosed 메소드는 입력 받은 팀 종료 여부와 Sort 조건에 따라 필터링된 List를 반환한다.")
    void findAllByIsClosed() {
        // given
        final Team team1 = saveTeam("잠실 네오조");
        final Team team2 = saveTeam("잠실 제이슨조");
        final Team team3 = saveTeam("잠실 토미조");
        final Team team4 = saveTeam("잠실 브리조");

        team3.close(TimeFixture.AFTER_START_TIME);
        team4.close(TimeFixture.AFTER_START_TIME);

        final Sort sort = Sort.by(Direction.DESC, "createdAt");

        // when
        final List<Team> closedTeam = teamRepository.findAllByIsClosed(true, sort);

        // then
        assertThat(closedTeam).containsExactly(team4, team3);
    }
}
