package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.dto.query.AllTeamDetailQueryResult;
import com.woowacourse.levellog.team.dto.query.AllTeamListDetailQueryResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TeamQueryRepository의")
class TeamQueryRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByTeamId 메서드는 참관자를 포함한 팀 참가자들의 상세 정보를 조회한다.")
    void findAllByTeamId() {
        // given
        final Member roma = saveMember("로마");
        final Member pep = saveMember("페퍼");
        final Member rick = saveMember("릭");
        final Member jun = saveMember("준");

        final Team team = saveTeam(roma, List.of(jun), pep, rick);

        final Levellog romaLevellog = saveLevellog(roma, team);
        final Levellog pepLevellog = saveLevellog(pep, team);
        final Levellog rickLevellog = saveLevellog(rick, team);

        final PreQuestion pepPreQuestion = savePreQuestion(pepLevellog, roma);
        final PreQuestion rickPreQuestion = savePreQuestion(rickLevellog, roma);

        // when
        final List<AllTeamDetailQueryResult> actual = teamQueryRepository.findAllByTeamId(team.getId(), roma.getId());

        // then
        assertThat(actual).hasSize(4)
                .extracting("memberId", "levellogId", "preQuestionId", "nickname", "isWatcher", "isHost")
                .containsExactly(
                        tuple(roma.getId(), romaLevellog.getId(), null, "로마", false, true),
                        tuple(pep.getId(), pepLevellog.getId(), pepPreQuestion.getId(), "페퍼", false, false),
                        tuple(rick.getId(), rickLevellog.getId(), rickPreQuestion.getId(), "릭", false, false),
                        tuple(jun.getId(), null, null, "준", true, false)
                );

    }

    @Test
    @DisplayName("findAllList 메서드는 isClosed=false 일 때 종료되지 않은 팀 목록을 조회한다.")
    void findAllList_open_success() {
        // given
        // 팀 1
        final Member roma = saveMember("로마");
        final Member pep = saveMember("페퍼");
        final Member rick = saveMember("릭");
        final Member jun = saveMember("준");

        final Team team1 = saveTeam(roma, List.of(jun), pep, rick);
        team1.close(TimeFixture.AFTER_START_TIME);

        // 팀 2
        final Member eve = saveMember("이브");
        final Member alien = saveMember("알린");

        final Team team2 = saveTeam(eve, alien);

        // 팀 3
        final Member harry = saveMember("해리");
        final Member kyoul = saveMember("결");

        final Team team3 = saveTeam(harry, kyoul);

        teamRepository.flush();

        // when
        final List<AllTeamListDetailQueryResult> actual = teamQueryRepository.findAllList(false, 10, 0);

        // then
        assertThat(actual).hasSize(4)
                .extracting("id", "memberId")
                .containsExactly(
                        tuple(team3.getId(), harry.getId()),
                        tuple(team3.getId(), kyoul.getId()),

                        tuple(team2.getId(), eve.getId()),
                        tuple(team2.getId(), alien.getId())
                );
    }

    @Test
    @DisplayName("findAllList 메서드는 isClosed=true 일 때 종료된 팀 목록을 조회한다.")
    void findAllList_close_success() {
        // given
        // 팀 1
        final Member roma = saveMember("로마");
        final Member pep = saveMember("페퍼");
        final Member rick = saveMember("릭");
        final Member jun = saveMember("준");

        final Team team1 = saveTeam(roma, List.of(jun), pep, rick);

        // 팀 2
        final Member eve = saveMember("이브");
        final Member alien = saveMember("알린");

        final Team team2 = saveTeam(eve, alien);
        team2.close(TimeFixture.AFTER_START_TIME);

        // 팀 3
        final Member harry = saveMember("해리");
        final Member kyoul = saveMember("결");

        final Team team3 = saveTeam(harry, kyoul);
        team3.close(TimeFixture.AFTER_START_TIME);

        teamRepository.flush();

        // when
        final List<AllTeamListDetailQueryResult> actual = teamQueryRepository.findAllList(true, 10, 0);

        // then
        assertThat(actual).hasSize(4)
                .extracting("id", "memberId")
                .containsExactly(
                        tuple(team3.getId(), harry.getId()),
                        tuple(team3.getId(), kyoul.getId()),

                        tuple(team2.getId(), eve.getId()),
                        tuple(team2.getId(), alien.getId())
                );
    }

    @Test
    @DisplayName("findMyList 메서드는 내가 속한 팀 목록을 조회한다.")
    void findMyList() {
        // given
        // 팀 1
        final Member pepper = saveMember("페퍼");
        final Member alien = saveMember("알린");

        final Team team1 = saveTeam(pepper, alien);

        // 팀 2
        final Member kyoul = saveMember("결");

        final Team team2 = saveTeam(pepper, kyoul);

        // when
        final List<AllTeamListDetailQueryResult> actual = teamQueryRepository.findMyList(pepper);

        // then
        assertThat(actual).hasSize(4)
                .extracting("id", "memberId")
                .containsExactly(
                        tuple(team2.getId(), pepper.getId()),
                        tuple(team2.getId(), kyoul.getId()),

                        tuple(team1.getId(), pepper.getId()),
                        tuple(team1.getId(), alien.getId())
                );
    }
}
