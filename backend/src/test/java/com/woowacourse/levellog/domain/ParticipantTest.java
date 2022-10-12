package com.woowacourse.levellog.domain;

import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantsFactory;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Participant의")
class ParticipantTest {

    @ParameterizedTest
    @CsvSource(value = {"0,true", "2,false"})
    @DisplayName("isParticipant 메서드는 참가자인지 여부를 반환한다.")
    void isParticipant(final int index, final boolean expected) {
        // given
        final TeamDetail teamDetail = new TeamDetail("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com", 1);
        final Team team = MockEntityFactory.setId(1L, new Team(teamDetail, 1L, List.of(1L, 2L), List.of(3L)));

        final Participant participant = team.getParticipants().getValues().get(index);

        // when
        final boolean actual = participant.isParticipant();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
