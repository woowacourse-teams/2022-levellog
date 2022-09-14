package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TeamCustomRepository의")
class TeamCustomRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllParticipants 메서드는 참관자를 제외한 팀 참가자들의 상세 정보를 조회한다.")
    void findAllParticipants() {
        // given
        final Member roma = saveMember("로마");
        final Member pepper = saveMember("페퍼");
        final Member rick = saveMember("릭");
        final Member jun = saveMember("준");

        final Team team = saveTeam(roma, List.of(jun), pepper, rick);

        final Levellog romaLevellog = saveLevellog(roma, team);
        final Levellog pepperLevellog = saveLevellog(pepper, team);
        final Levellog rickLevellog = saveLevellog(rick, team);

        final PreQuestion pepperPreQuestion = savePreQuestion(pepperLevellog, roma);
        final PreQuestion rickPreQuestion = savePreQuestion(rickLevellog, roma);

        // when
        final List<ParticipantDto> actual = teamCustomRepository.findAllParticipants(team, roma.getId());

        // then
        assertThat(actual).hasSize(3)
                .extracting("memberId", "levellogId", "preQuestionId", "nickname")
                .containsExactly(
                        tuple(roma.getId(), romaLevellog.getId(), null, "로마"),
                        tuple(pepper.getId(), pepperLevellog.getId(), pepperPreQuestion.getId(), "페퍼"),
                        tuple(rick.getId(), rickLevellog.getId(), rickPreQuestion.getId(), "릭")
                );
    }
}
