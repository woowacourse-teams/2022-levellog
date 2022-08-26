package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.teamdisplay.domain.ParticipantDetail;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TeamDisplayRepository의")
class TeamDisplayRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllParticipantDetail 메서드는 팀에 해당하는 ParticipantDetail을 조회한다. - 릭이 요청한 경우")
    void success() {
        // given
        final Member roma = saveMember("로마");
        final Member pepper = saveMember("페퍼");
        final Member alien = saveMember("알린");
        final Member rick = saveMember("릭");
        final Member eve = saveMember("이브");
        final Member pobi = saveMember("포비");

        final Team team = saveTeam(roma, List.of(pobi), roma, pepper, alien, rick, eve);

        final Levellog romaLevellog = saveLevellog(roma, team);
        final Levellog pepperLevellog = saveLevellog(pepper, team);
        final Levellog alienLevellog = saveLevellog(alien, team);
        final Levellog rickLevellog = saveLevellog(rick, team);
        final Levellog eveLevellog = saveLevellog(eve, team);

        final PreQuestion preQuestionToRoma = savePreQuestion(romaLevellog, rick);
        final PreQuestion preQuestionToPepper = savePreQuestion(pepperLevellog, rick);

        savePreQuestion(pepperLevellog, roma);
        savePreQuestion(alienLevellog, roma);
        savePreQuestion(rickLevellog, pepper);
        savePreQuestion(alienLevellog, pepper);
        savePreQuestion(rickLevellog, pepper);
        savePreQuestion(eveLevellog, alien);

        // when
        final List<ParticipantDetail> actual = teamDisplayRepository.findAllParticipantDetail(rick, team);

        // then
        assertThat(actual).hasSize(5)
                .extracting("nickname", "levellogId", "preQuestionId")
                .containsExactly(
                        tuple("로마", romaLevellog.getId(), preQuestionToRoma.getId()),
                        tuple("페퍼", pepperLevellog.getId(), preQuestionToPepper.getId()),
                        tuple("알린", alienLevellog.getId(), null),
                        tuple("릭", rickLevellog.getId(), null),
                        tuple("이브", eveLevellog.getId(), null)
                );
    }
}
