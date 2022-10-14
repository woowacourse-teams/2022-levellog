package com.woowacourse.levellog.domain;


import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participants;
import com.woowacourse.levellog.team.domain.ParticipantsFactory;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Participants의")
class ParticipantsTest {

    @Nested
    @DisplayName("toInterviewRole 메서드는")
    class ToInterviewRole {

        @ParameterizedTest(name = "참가자 아이디가 [1, 2, 3, 4, 5]이고 인터뷰어 수가 2명인 팀에서 아이디가 {0}인 타겟 멤버에 대해 아이디가 {1}인 참가자의 인터뷰 역할은 {2}이다.")
        @CsvSource(value = {"1,2,INTERVIEWER", "1,3,INTERVIEWER", "1,4,OBSERVER", "1,5,OBSERVER"})
        void success(final Long targetMemberId, final Long memberId, final InterviewRole expected) {
            // given
            final int interviewerNumber = 2;
            final TeamDetail teamDetail = new TeamDetail("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com",
                    interviewerNumber);
            final List<Long> participantIds = List.of(1L, 2L, 3L, 4L, 5L);
            final Team team = MockEntityFactory.setId(1L, new Team(teamDetail, 1L, participantIds,
                    Collections.emptyList()));

            final Participants participants = team.getParticipants();

            // when
            final InterviewRole actual = participants.toInterviewRole(targetMemberId, memberId, interviewerNumber);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("타겟 멤버가 요청한 멤버와 같으면 ME를 반환한다.")
        void toInterviewRole_me_success() {
            // given
            final int interviewerNumber = 2;
            final TeamDetail teamDetail = new TeamDetail("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com",
                    interviewerNumber);
            final List<Long> participantIds = List.of(1L, 2L, 3L, 4L, 5L);
            final Team team = MockEntityFactory.setId(1L, new Team(teamDetail, 1L, participantIds,
                    Collections.emptyList()));

            final Participants participants = team.getParticipants();

            // when
            final InterviewRole actual = participants.toInterviewRole(1L, 1L, interviewerNumber);

            // then
            assertThat(actual).isEqualTo(InterviewRole.ME);
        }
    }
}
