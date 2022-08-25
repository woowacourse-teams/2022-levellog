package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.BEFORE_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.teamdisplay.dto.TeamDto;
import com.woowacourse.levellog.teamdisplay.dto.TeamListDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamDisplayService의")
class TeamDisplayServiceTest extends ServiceTest {

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("전체 팀 목록을 인터뷰 진행 상태(시작 전 or 진행 중 -> 종료)와 최근 생성일 순으로 정렬하여 조회한다.")
        void findAll_allTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Team pepperTeam = saveTeam(pepper, roma);
            final Team rickTeam = saveTeam(rick, pepper);
            final Team romaTeam = saveTeam(roma, rick);

            rickTeam.close(AFTER_START_TIME);

            //when
            final TeamListDto response = teamDisplayService.findAll(Optional.empty(), rick.getId());

            //then
            assertAll(
                    () -> assertThat(toTitles(response))
                            .containsExactly(romaTeam.getTitle(), pepperTeam.getTitle(), rickTeam.getTitle()),
                    () -> assertThat(toHostIds(response)).containsExactly(roma.getId(), pepper.getId(), rick.getId()),
                    () -> assertThat(toParticipantsSizes(response)).containsExactly(2, 2, 2),
                    () -> assertThat(toCloseStatuses(response))
                            .containsExactly(TeamStatus.READY, TeamStatus.READY, TeamStatus.CLOSED),
                    () -> assertThat(toIsParticipants(response)).containsExactly(true, false, true),
                    () -> assertThat(response.getTeams()).hasSize(3)
            );
        }

        @Test
        @DisplayName("인터뷰 시작 전인 팀의 목록을 최근 생성일 순으로 조회한다.")
        void findAll_ReadyTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");

            saveTeam(TEAM_START_TIME, pepper, roma);
            final Team rickTeam = saveTeam(TEAM_START_TIME.plusDays(3), rick, pepper);
            final Team eveTeam = saveTeam(TEAM_START_TIME.plusDays(3), eve, alien);
            final Team romaTeam = saveTeam(roma, rick);

            timeStandard.setInProgress();
            romaTeam.close(AFTER_START_TIME);

            //when
            final TeamListDto response = teamDisplayService.findAll(Optional.of("ready"), rick.getId());

            //then
            assertAll(
                    () -> assertThat(toTitles(response)).containsExactly(eveTeam.getTitle(), rickTeam.getTitle()),
                    () -> assertThat(toHostIds(response)).containsExactly(eve.getId(), rick.getId()),
                    () -> assertThat(toParticipantsSizes(response)).containsExactly(2, 2),
                    () -> assertThat(toCloseStatuses(response)).containsExactly(TeamStatus.READY, TeamStatus.READY),
                    () -> assertThat(toIsParticipants(response)).containsExactly(false, true),
                    () -> assertThat(response.getTeams()).hasSize(2)
            );
        }

        @Test
        @DisplayName("인터뷰 진행 중인 팀의 목록을 최근 생성일 순으로 조회한다.")
        void findAll_InProgressTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");

            final Team pepperTeam = saveTeam(TEAM_START_TIME, pepper, roma);
            final Team rickTeam = saveTeam(TEAM_START_TIME, rick, pepper);
            saveTeam(TEAM_START_TIME.plusDays(3), eve, alien);
            final Team romaTeam = saveTeam(roma, rick);

            timeStandard.setInProgress();
            romaTeam.close(AFTER_START_TIME);

            //when
            final TeamListDto response = teamDisplayService.findAll(Optional.of("in-progress"), rick.getId());

            //then
            assertAll(
                    () -> assertThat(toTitles(response)).containsExactly(rickTeam.getTitle(), pepperTeam.getTitle()),
                    () -> assertThat(toHostIds(response)).containsExactly(rick.getId(), pepper.getId()),
                    () -> assertThat(toParticipantsSizes(response)).containsExactly(2, 2),
                    () -> assertThat(toCloseStatuses(response))
                            .containsExactly(TeamStatus.IN_PROGRESS, TeamStatus.IN_PROGRESS),
                    () -> assertThat(toIsParticipants(response)).containsExactly(true, false),
                    () -> assertThat(response.getTeams()).hasSize(2)
            );
        }

        @Test
        @DisplayName("인터뷰가 종료된 팀의 목록을 최근 생성일 순으로 조회한다.")
        void findAll_closedTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");

            saveTeam(pepper, roma);
            final Team rickTeam = saveTeam(rick, pepper);
            final Team eveTeam = saveTeam(eve, alien);

            rickTeam.close(AFTER_START_TIME);
            eveTeam.close(AFTER_START_TIME);

            //when
            final TeamListDto response = teamDisplayService.findAll(Optional.of("closed"), rick.getId());

            //then
            assertAll(
                    () -> assertThat(toTitles(response)).containsExactly(eveTeam.getTitle(), rickTeam.getTitle()),
                    () -> assertThat(toHostIds(response)).containsExactly(eve.getId(), rick.getId()),
                    () -> assertThat(toParticipantsSizes(response)).containsExactly(2, 2),
                    () -> assertThat(toCloseStatuses(response)).containsExactly(TeamStatus.CLOSED, TeamStatus.CLOSED),
                    () -> assertThat(toIsParticipants(response)).containsExactly(false, true),
                    () -> assertThat(response.getTeams()).hasSize(2)
            );
        }

        @Test
        @DisplayName("잘못된 팀 Status를 받으면 예외가 발생한다.")
        void findAll_invalidStatus_exception() {
            // given
            final Long rickId = saveMember("릭").getId();

            // when & then
            final Optional<String> invalidStatus = Optional.of("invalid");
            assertThatThrownBy(() -> teamDisplayService.findAll(invalidStatus, rickId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력 받은 status가 올바르지 않습니다.");
        }

        @Test
        @DisplayName("삭제된 팀을 제외한 팀 목록을 조회한다.")
        void findAll_exceptDeletedTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            saveTeam(pepper, roma);
            final Team team = saveTeam(rick, pepper);

            team.delete(BEFORE_START_TIME);

            //when
            final TeamListDto response = teamDisplayService.findAll(Optional.empty(), rick.getId());

            //then
            assertThat(response.getTeams()).hasSize(1);
        }

        private List<String> toTitles(final TeamListDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getTitle)
                    .collect(Collectors.toList());
        }

        private List<Long> toHostIds(final TeamListDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getHostId)
                    .collect(Collectors.toList());
        }

        private List<Integer> toParticipantsSizes(final TeamListDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getParticipants)
                    .map(List::size)
                    .collect(Collectors.toList());
        }

        private List<TeamStatus> toCloseStatuses(final TeamListDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getStatus)
                    .collect(Collectors.toList());
        }

        private List<Boolean> toIsParticipants(final TeamListDto response) {
            return response.getTeams()
                    .stream().map(TeamDto::getIsParticipant)
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("findByTeamIdAndMemberId 메서드는")
    class FindByTeamIdAndMemberId {

        @Test
        @DisplayName("id에 해당하는 팀을 조회한다.")
        void success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            //when
            final TeamDto response = teamDisplayService.findByTeamIdAndMemberId(team.getId(), rick.getId());

            //then
            assertAll(
                    () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                    () -> assertThat(response.getHostId()).isEqualTo(rick.getId()),
                    () -> assertThat(response.getStatus()).isEqualTo(TeamStatus.READY),
                    () -> assertThat(response.getParticipants()).hasSize(2)
            );
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 조회하면 예외를 던진다.")
        void findByTeamIdAndMemberId_notFound_exception() {
            // when & then
            assertThatThrownBy(() -> teamDisplayService.findByTeamIdAndMemberId(1000L, 1L))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Nested
        @DisplayName("요청한 유저가 팀에 참가자일 때")
        class ParticipantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이, isParticipant를 true로 응답한다.")
            void findByTeamIdAndMemberId_intervieweeAndInterviewer_success() {
                //given
                final Member rick = saveMember("릭");
                final Member pepper = saveMember("페퍼");
                final Member roma = saveMember("로마");
                final Member alien = saveMember("알린");
                final Member eve = saveMember("이브");

                final Team team = saveTeam(2, rick, pepper, roma, alien, eve);

                //when
                final TeamDto responseOfPepper = teamDisplayService.findByTeamIdAndMemberId(team.getId(),
                        pepper.getId());
                final TeamDto responseOfEve = teamDisplayService.findByTeamIdAndMemberId(team.getId(),
                        eve.getId());

                //then
                assertAll(
                        () -> assertThat(responseOfPepper.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(responseOfPepper.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(responseOfPepper.getParticipants()).hasSize(5),
                        () -> assertThat(responseOfPepper.getIsParticipant()).isTrue(),

                        () -> assertThat(responseOfPepper.getInterviewers()).containsExactly(roma.getId(),
                                alien.getId()),
                        () -> assertThat(responseOfPepper.getInterviewees()).containsExactly(eve.getId(), rick.getId()),

                        () -> assertThat(responseOfEve.getInterviewers()).containsExactly(rick.getId(), pepper.getId()),
                        () -> assertThat(responseOfEve.getInterviewees()).containsExactly(roma.getId(), alien.getId()),
                        () -> assertThat(responseOfPepper.getIsParticipant()).isTrue()

                );
            }

            @Test
            @DisplayName("참가자가 3명이고, 인터뷰어 수는 2명이면 인터뷰어와 인터뷰이가 동일하다.")
            void findByTeamIdAndMemberId_manyInterviewerNumber_success() {
                //given
                final Member rick = saveMember("릭");
                final Member pepper = saveMember("페퍼");
                final Member roma = saveMember("로마");

                final Team team = saveTeam(2, rick, pepper, roma);

                //when
                final TeamDto response = teamDisplayService.findByTeamIdAndMemberId(team.getId(), pepper.getId());

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(response.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(response.getParticipants()).hasSize(3),

                        () -> assertThat(response.getInterviewers()).isEqualTo(response.getInterviewees()),
                        () -> assertThat(response.getInterviewers()).containsExactly(roma.getId(), rick.getId()),
                        () -> assertThat(response.getInterviewees()).containsExactly(roma.getId(), rick.getId())
                );
            }
        }

        @Nested
        @DisplayName("요청한 유저가 팀의 참관자일 때")
        class WatcherRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이가 없고, isParticipant를 true로 응답한다.")
            void success() {
                //given
                final Member rick = saveMember("릭");
                final Member pepper = saveMember("페퍼");
                final Member pobi = saveMember("포비");

                final Team team = saveTeam(1, rick, List.of(pobi), pepper);

                //when
                final TeamDto response = teamDisplayService.findByTeamIdAndMemberId(team.getId(), pobi.getId());

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(response.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(response.getParticipants()).hasSize(2),
                        () -> assertThat(response.getWatchers()).hasSize(1),
                        () -> assertThat(response.getIsParticipant()).isTrue(),
                        () -> assertThat(response.getInterviewers()).isEmpty(),
                        () -> assertThat(response.getInterviewees()).isEmpty()
                );
            }
        }

        @Nested
        @DisplayName("요청한 유저가 팀에 참가자가 아닐 때")
        class NotParticipantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이가 빈 상태이고 isParticipant를 false로 응답한다.")
            void success() {
                //given
                final Member rick = saveMember("릭");
                final Member pepper = saveMember("페퍼");
                final Member roma = saveMember("로마");
                final Member alien = saveMember("알린");

                final Team team = saveTeam(rick, pepper, roma);

                //when
                final TeamDto response = teamDisplayService.findByTeamIdAndMemberId(team.getId(), alien.getId());

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(response.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(response.getParticipants()).hasSize(3),
                        () -> assertThat(response.getInterviewers()).isEmpty(),
                        () -> assertThat(response.getInterviewees()).isEmpty(),
                        () -> assertThat(response.getIsParticipant()).isFalse()
                );
            }
        }
    }

    @Nested
    @DisplayName("findAllByMemberId 메서드는 ")
    class FindAllByMemberId {

        @Test
        @DisplayName("주어진 memberId의 멤버가 참가한 모든 팀을 조회한다.")
        void success() {
            // given
            final Member roma = saveMember("로마");
            final Member harry = saveMember("해리");
            final Member alien = saveMember("알린");

            saveTeam(roma, harry);
            saveTeam(roma, harry, alien);
            saveTeam(harry, alien);

            // when
            final List<TeamDto> teams = teamDisplayService.findAllByMemberId(roma.getId()).getTeams();

            // then
            assertThat(teams).hasSize(2);
        }

        @Test
        @DisplayName("주어진 memberId의 멤버가 존재하지 않을 때 예외를 던진다.")
        void findAllByMemberId_memberNotFound_exception() {
            // when & then
            assertThatThrownBy(() -> teamDisplayService.findAllByMemberId(100_000L))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않습니다.", String.valueOf(100_000L));
        }
    }
}
