package com.woowacourse.levellog.domain;


import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;

import com.woowacourse.levellog.team.domain.ParticipantsIngredient;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Participants의")
class ParticipantsTest {

    private Team saveTeam() {
        final TeamDetail teamDetail = new TeamDetail("네오와 함께하는 레벨 인터뷰", "선릉 트랙룸", TEAM_START_TIME, "profileUrl", 2);
        final ParticipantsIngredient participantsIngredient = new ParticipantsIngredient(1L, List.of(1L, 2L, 3L),
                List.of(4L, 5L, 6L));

        return new Team(teamDetail, participantsIngredient);
    }

    private List<Long> toIdList(final String s) {
        return Arrays.stream(s.split(", "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

//    @Nested
//    @DisplayName("toInterviewRole 메서드는")
//    class ToInterviewRole {
//
//        @ParameterizedTest(name = "참가자 아이디가 [1, 2, 3, 4, 5]이고 인터뷰어 수가 2명인 팀에서 아이디가 {0}인 타겟 멤버에 대해 아이디가 {1}인 참가자의 인터뷰 역할은 {2}이다.")
//        @CsvSource(value = {"1,2,INTERVIEWER", "1,3,INTERVIEWER", "1,4,OBSERVER", "1,5,OBSERVER"})
//        void success(final Long targetMemberId, final Long memberId, final InterviewRole expected) {
//            // given
//            final int interviewerNumber = 2;
//            final Team team = MockEntityFactory.setId(1L,
//                    new Team("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com", interviewerNumber));
//
//            final List<Participant> values = List.of(
//                    new Participant(null, 1L, true, false),
//                    new Participant(null, 2L, false, false),
//                    new Participant(null, 3L, false, false),
//                    new Participant(null, 4L, false, false),
//                    new Participant(null, 5L, false, false));
//            final Participants participants = new Participants(values);
//
//            // when
//            final InterviewRole actual = participants.toInterviewRole(team.getId(), targetMemberId, memberId,
//                    interviewerNumber);
//
//            // then
//            assertThat(actual).isEqualTo(expected);
//        }
//
//        @Test
//        @DisplayName("타겟 멤버가 요청한 멤버와 같으면 ME를 반환한다.")
//        void toInterviewRole_me_success() {
//            // given
//            final int interviewerNumber = 2;
//            final Team team = MockEntityFactory.setId(1L,
//                    new Team("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com", interviewerNumber));
//            final List<Participant> values = List.of(
//                    new Participant(team, 1L, true, false),
//                    new Participant(team, 2L, false, false),
//                    new Participant(team, 3L, false, false),
//                    new Participant(team, 4L, false, false),
//                    new Participant(team, 5L, false, false));
//            final Participants participants = new Participants(values);
//
//            // when
//            final InterviewRole actual = participants.toInterviewRole(team.getId(), 1L, 1L, interviewerNumber);
//
//            // then
//            assertThat(actual).isEqualTo(InterviewRole.ME);
//        }
//
//        @Test
//        @DisplayName("요청한 멤버가 참가자가 아니라면 예외를 던진다.")
//        void toInterviewRole_requestMemberIdNotContains_exception() {
//            // given
//            final int interviewerNumber = 2;
//            final Team team = MockEntityFactory.setId(1L,
//                    new Team("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com", interviewerNumber));
//
//            final List<Participant> values = List.of(
//                    new Participant(team, 1L, true, false),
//                    new Participant(team, 2L, false, false),
//                    new Participant(team, 3L, false, false),
//                    new Participant(team, 4L, false, false),
//                    new Participant(team, 5L, false, false));
//            final Participants participants = new Participants(values);
//
//            // when & then
//            assertThatThrownBy(() -> participants.toInterviewRole(team.getId(), 1L, 9L, interviewerNumber))
//                    .isInstanceOf(ParticipantNotSameTeamException.class);
//        }
//
//        @Test
//        @DisplayName("타겟 멤버가 참가자가 아니라면 예외를 던진다.")
//        void toInterviewRole_targetMemberIdNotContains_exception() {
//            // given
//            final int interviewerNumber = 2;
//            final Team team = MockEntityFactory.setId(1L,
//                    new Team("레벨로그팀", "선릉 트랙룸", TEAM_START_TIME, "레벨로그팀.com", interviewerNumber));
//
//            final List<Participant> values = List.of(
//                    new Participant(team, 1L, true, false),
//                    new Participant(team, 2L, false, false),
//                    new Participant(team, 3L, false, false),
//                    new Participant(team, 4L, false, false),
//                    new Participant(team, 5L, false, false));
//            final Participants participants = new Participants(values);
//
//            // when & then
//            assertThatThrownBy(() -> participants.toInterviewRole(team.getId(), 9L, 1L, interviewerNumber))
//                    .isInstanceOf(ParticipantNotFoundException.class);
//        }
//    }
}
