package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.AllSimpleParticipantDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TeamCustomRepository의")
class TeamCustomRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllParticipants 메서드는 참관자를 포함한 팀 참가자들의 상세 정보를 조회한다.")
    void findAllParticipants() {
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
        final List<AllParticipantDto> actual = teamCustomRepository.findAllByTeamId(team.getId(), roma.getId());

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
    @DisplayName("findAllSimple 메서드는 개선된 쿼리로 팀 목록을 조회한다.")
    void findAllSimple() {
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

        // 팀 3
        final Member harry = saveMember("해리");
        final Member kyoul = saveMember("결");

        final Team team3 = saveTeam(harry, kyoul);

        // when
        final List<AllSimpleParticipantDto> actual = teamCustomRepository.findAllSimple(10, 0);

        // then
        assertThat(actual).hasSize(7)
                .extracting("id", "memberImage")
                .containsExactly(
                        tuple(team3.getId(), harry.getProfileUrl()),
                        tuple(team3.getId(), kyoul.getProfileUrl()),

                        tuple(team2.getId(), eve.getProfileUrl()),
                        tuple(team2.getId(), alien.getProfileUrl()),

                        tuple(team1.getId(), roma.getProfileUrl()),
                        tuple(team1.getId(), pep.getProfileUrl()),
                        tuple(team1.getId(), rick.getProfileUrl())
                );
    }

    @Test
    @DisplayName("findAllMy 메서드는 내가 속한 팀 목록을 조회한다.")
    void findAllMy() {
        // given
        // 팀 1
        final Member pep = saveMember("페퍼");
        final Member ali = saveMember("알린");

        final Team team1 = saveTeam(pep, ali);

        final Levellog pepLevellog1 = saveLevellog(pep, team1);
        final Levellog aliLevellog = saveLevellog(ali, team1);

        final PreQuestion aliPreQuestion = savePreQuestion(aliLevellog, pep);

        // 팀 2
        final Member kyoul = saveMember("결");

        final Team team2 = saveTeam(pep, kyoul);

        final Levellog pepLevellog2 = saveLevellog(pep, team2);
        final Levellog kyoulLevellog = saveLevellog(kyoul, team2);

        final PreQuestion kyoulPreQuestion = savePreQuestion(kyoulLevellog, pep);

        // when
        final List<AllParticipantDto> actual = teamCustomRepository.findAllMy(pep);

        // then
        assertThat(actual).hasSize(4)
                .extracting("team", "memberId", "levellogId", "preQuestionId", "nickname", "isWatcher", "isHost")
                .containsExactly(
                        tuple(team2, pep.getId(), pepLevellog2.getId(), null, "페퍼", false, true),
                        tuple(team2, kyoul.getId(), kyoulLevellog.getId(), kyoulPreQuestion.getId(), "결", false, false),

                        tuple(team1, pep.getId(), pepLevellog1.getId(), null, "페퍼", false, true),
                        tuple(team1, ali.getId(), aliLevellog.getId(), aliPreQuestion.getId(), "알린", false, false)
                );
    }
}
