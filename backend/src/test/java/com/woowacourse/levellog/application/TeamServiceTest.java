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
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamStatusDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DisplayName("TeamService의")
class TeamServiceTest extends ServiceTest {

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
            final TeamsDto response = teamService.findAll(getPageRequest(), "all", rick.getId());

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
            final TeamsDto response = teamService.findAll(getPageRequest(), "ready", rick.getId());

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
            final TeamsDto response = teamService.findAll(getPageRequest(), "in-progress", rick.getId());

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
            final TeamsDto response = teamService.findAll(getPageRequest(), "closed", rick.getId());

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
            final Member rick = saveMember("릭");

            // when & then
            assertThatThrownBy(() -> teamService.findAll(getPageRequest(), "invalid", rick.getId()))
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
            final TeamsDto response = teamService.findAll(getPageRequest(), "all", rick.getId());

            //then
            assertThat(response.getTeams()).hasSize(1);
        }

        private PageRequest getPageRequest() {
            final Sort sort = Sort.by(
                    Sort.Order.asc("isClosed"),
                    Sort.Order.desc("createdAt")
            );

            return PageRequest.of(0, 20, sort);
        }

        private List<String> toTitles(final TeamsDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getTitle)
                    .collect(Collectors.toList());
        }

        private List<Long> toHostIds(final TeamsDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getHostId)
                    .collect(Collectors.toList());
        }

        private List<Integer> toParticipantsSizes(final TeamsDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getParticipants)
                    .map(List::size)
                    .collect(Collectors.toList());
        }

        private List<TeamStatus> toCloseStatuses(final TeamsDto response) {
            return response.getTeams()
                    .stream()
                    .map(TeamDto::getStatus)
                    .collect(Collectors.toList());
        }

        private List<Boolean> toIsParticipants(final TeamsDto response) {
            return response.getTeams()
                    .stream().map(TeamDto::getIsParticipant)
                    .collect(Collectors.toList());
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("팀을 생성한다.")
        void success() {
            //given
            final Long alien = saveMember("알린").getId();
            final Long pepper = saveMember("페퍼").getId();
            final Long roma = saveMember("로마").getId();

            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 2, TEAM_START_TIME,
                    List.of(alien, pepper, roma), Collections.emptyList());
            //when
            final Long id = teamService.save(teamDto, alien);

            //then
            final Optional<Team> team = teamRepository.findById(id);
            assertThat(team).isPresent();
        }

        @Test
        @DisplayName("참가자가 중복되면 예외가 발생한다.")
        void save_duplicateParticipants_exception() {
            //given
            final Long alien = saveMember("알린").getId();
            final Long pepper = saveMember("페퍼").getId();
            final Long roma = saveMember("로마").getId();

            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper, roma, roma), Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, alien))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참가자가 존재합니다.");
        }

        @Test
        @DisplayName("참관자가 중복되면 예외가 발생한다.")
        void save_duplicateWatchers_exception() {
            //given
            final Long alien = saveMember("알린").getId();
            final Long pepper = saveMember("페퍼").getId();
            final Long roma = saveMember("로마").getId();

            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper), List.of(roma, roma));

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, alien))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참관자가 존재합니다.");
        }

        @Test
        @DisplayName("참가자와 참관자에 중복되는 멤버가 있으면 예외가 발생한다.")
        void save_notIndependent_exception() {
            //given
            final Long alien = saveMember("알린").getId();
            final Long pepper = saveMember("페퍼").getId();
            final Long roma = saveMember("로마").getId();

            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper, roma), List.of(roma));

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, alien))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자와 참관자에 모두 포함된 멤버가 존재합니다.");
        }

        @Test
        @DisplayName("호스트가 참가자 또는 참관자에 포함되지 않으면 예외가 발생한다.")
        void save_hostExistence_exception() {
            //given
            final Long alien = saveMember("알린").getId();
            final Long pepper = saveMember("페퍼").getId();
            final Long roma = saveMember("로마").getId();
            final Long rick = saveMember("릭").getId();

            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper, roma), List.of(rick));

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, alien))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("findMyRole 메서드는")
    class FindMyRole {

        @Test
        @DisplayName("팀의 참가자에 대한 나의 역할을 조회한다. - interviewer")
        void findMyRole_interviewer_success() {
            // given
            final Member rick = saveMember("릭");
            final Member harry = saveMember("해리");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, harry, alien);

            final Long teamId = team.getId();
            final Long targetMemberId = rick.getId();
            final Long requestMemberId = harry.getId();

            // when
            final InterviewRoleDto actual = teamService.findMyRole(teamId, targetMemberId, requestMemberId);

            // then
            assertThat(actual.getMyRole()).isEqualTo(InterviewRole.INTERVIEWER);
        }

        @Test
        @DisplayName("팀의 참가자에 대한 나의 역할을 조회한다. - observer")
        void findMyRole_observer_success() {
            // given
            final Member rick = saveMember("릭");
            final Member harry = saveMember("해리");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, harry, alien);

            final Long teamId = team.getId();
            final Long targetMemberId = rick.getId();
            final Long requestMemberId = alien.getId();

            // when
            final InterviewRoleDto actual = teamService.findMyRole(teamId, targetMemberId, requestMemberId);

            // then
            assertThat(actual.getMyRole()).isEqualTo(InterviewRole.OBSERVER);
        }

        @Test
        @DisplayName("팀의 참가자가 아닌 member가 요청하면 예외를 던진다.")
        void findMyRole_iAmNotParticipant_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member harry = saveMember("해리");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, harry);

            final Long teamId = team.getId();
            final Long targetMemberId = rick.getId();
            final Long requestMemberId = alien.getId();

            // when & then
            assertThatThrownBy(() -> teamService.findMyRole(teamId, targetMemberId, requestMemberId))
                    .isInstanceOf(ParticipantNotSameTeamException.class);
        }

        @Test
        @DisplayName("targetMember가 팀의 참가자가 아니면 예외를 던진다.")
        void findMyRole_targetNotParticipant_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member harry = saveMember("해리");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(rick, harry);

            final Long teamId = team.getId();
            final Long targetMemberId = alien.getId();
            final Long requestMemberId = rick.getId();

            // when & then
            assertThatThrownBy(() -> teamService.findMyRole(teamId, targetMemberId, requestMemberId))
                    .isInstanceOf(ParticipantNotFoundException.class);
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
            final TeamDto response = teamService.findByTeamIdAndMemberId(team.getId(), rick.getId());

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
            assertThatThrownBy(() -> teamService.findByTeamIdAndMemberId(1000L, 1L))
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
                final TeamDto responseOfPepper = teamService.findByTeamIdAndMemberId(team.getId(),
                        pepper.getId());
                final TeamDto responseOfEve = teamService.findByTeamIdAndMemberId(team.getId(),
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
                final TeamDto response = teamService.findByTeamIdAndMemberId(team.getId(), pepper.getId());

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
                final TeamDto response = teamService.findByTeamIdAndMemberId(team.getId(), pobi.getId());

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
                final TeamDto response = teamService.findByTeamIdAndMemberId(team.getId(), alien.getId());

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
    @DisplayName("findStatus 메서드는")
    class FindStatus {

        @Test
        @DisplayName("인터뷰 시작 전인 팀의 상태를 조회하면 READY를 반환한다.")
        void findStatus_ready_success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Team team = saveTeam(pepper, rick);

            // when
            final TeamStatusDto actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(TeamStatus.READY);
        }

        @Test
        @DisplayName("인터뷰 진행 중인 팀의 상태를 조회하면 IN_PROGRESS를 반환한다.")
        void findStatus_inProgress_success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Team team = saveTeam(pepper, rick);

            timeStandard.setInProgress();

            // when
            final TeamStatusDto actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(TeamStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("인터뷰 종료 후인 팀의 상태를 조회하면 CLOSED를 반환한다.")
        void findStatus_closed_success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Team team = saveTeam(pepper, rick);

            team.close(AFTER_START_TIME);

            // when
            final TeamStatusDto actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(TeamStatus.CLOSED);
        }

        @Test
        @DisplayName("id에 해당하는 팀이 존재하지 않으면 예외를 던진다.")
        void findStatus_notExistTeam_exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findStatus(999L))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("id에 해당하는 팀 정보를 변경한다.")
        void success() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");
            final Member roma = saveMember("로마");
            final Team team = saveTeam(rick, pepper, eve);

            final List<Long> participantsIds = List.of(rick.getId(), eve.getId(), alien.getId(), roma.getId());
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 2, AFTER_START_TIME,
                    participantsIds, Collections.emptyList());

            // when
            teamService.update(request, team.getId(), rick.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            final List<Long> actualParticipantsIds = participantRepository.findByTeam(actualTeam)
                    .stream()
                    .map(Participant::getMember)
                    .map(Member::getId)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(actualTeam.getTitle()).isEqualTo(request.getTitle()),
                    () -> assertThat(actualTeam.getPlace()).isEqualTo(request.getPlace()),
                    () -> assertThat(actualTeam.getStartAt()).isEqualTo(request.getStartAt()),
                    () -> assertThat(actualTeam.getInterviewerNumber()).isEqualTo(request.getInterviewerNumber()),
                    () -> assertThat(actualParticipantsIds).isEqualTo(
                            List.of(rick.getId(), eve.getId(), alien.getId(), roma.getId()))
            );
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 수정하는 경우 예외를 던진다.")
        void update_hostUnauthorized_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(rick, pepper);

            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, AFTER_START_TIME,
                    List.of(rick.getId()), Collections.emptyList());

            // when, then
            final Long memberId = pepper.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void update_teamNotFound_exception() {
            //given
            final Long memberId = saveMember("릭").getId();
            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    Collections.emptyList(), Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> teamService.update(request, 1000L, memberId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 팀을 수정하려고 하면 예외를 던진다.")
        void update_notReady_exception() {
            //given
            final Member member = saveMember("릭");
            final Team team = saveTeam(member);
            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(member.getId()), Collections.emptyList());
            timeStandard.setInProgress();

            //when & then
            final Long teamId = team.getId();
            final Long memberId = member.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.", teamId, team.getStartAt());
        }

        @Test
        @DisplayName("참가자가 중복되면 예외가 발생한다.")
        void update_duplicate_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper.getId(), pepper.getId()), Collections.emptyList());

            //when & then
            final Long memberId = alien.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참가자가 존재합니다.");
        }

        @Test
        @DisplayName("참관자가 중복되면 예외가 발생한다.")
        void update_duplicateWatchers_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien.getId(), pepper.getId()), List.of(roma.getId(), roma.getId()));

            //when & then
            final Long memberId = alien.getId();
            assertThatThrownBy(() -> teamService.update(teamDto, teamId, memberId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참관자가 존재합니다.");
        }

        @Test
        @DisplayName("참가자와 참관자에 중복되는 멤버가 있으면 예외가 발생한다.")
        void update_notIndependent_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien.getId(), pepper.getId(), roma.getId()), List.of(roma.getId()));

            //when & then
            final Long memberId = alien.getId();
            assertThatThrownBy(() -> teamService.update(teamDto, teamId, memberId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자와 참관자에 모두 포함된 멤버가 존재합니다.");
        }

        @Test
        @DisplayName("호스트가 참가자 또는 참관자에 포함되지 않으면 예외가 발생한다.")
        void update_hostExistence_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");
            final Member rick = saveMember("릭");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper.getId(), roma.getId()), List.of(rick.getId()));

            //when & then
            final Long memberId = alien.getId();
            assertThatThrownBy(() -> teamService.update(teamDto, teamId, memberId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("close 메서드는")
    class Close {

        @Test
        @DisplayName("입력 받은 팀의 인터뷰를 종료한다.")
        void success() {
            // given
            final Member rick = saveMember("릭");
            final Team team = saveTeam(rick);

            timeStandard.setInProgress();

            // when
            teamService.close(team.getId(), rick.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(actualTeam.isClosed()).isTrue();
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 인터뷰를 종료하면 예외가 발생한다.")
        void close_notHost_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member alien = saveMember("알린");
            final Long teamId = saveTeam(rick, alien).getId();

            // when & then
            final Long memberId = alien.getId();
            assertThatThrownBy(() -> teamService.close(teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContainingAll("호스트 권한이 없습니다.", memberId.toString());
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("id에 해당하는 팀을 deleted 상태로 만든다.")
        void success() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            // when
            teamService.deleteById(team.getId(), rick.getId());
            entityManager.flush();
            entityManager.clear();

            // then
            final Optional<Team> actualTeam = teamRepository.findById(team.getId());
            assertThat(actualTeam).isEmpty();
            final List<Participant> actualParticipants = participantRepository.findByTeam(team);
            assertThat(actualParticipants).isEmpty();
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 삭제하는 경우 예외를 던진다.")
        void delete_hostUnauthorized_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            // when, then
            final Long memberId = pepper.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.deleteById(teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void delete_teamNotFound_exception() {
            //given
            final Long memberId = saveMember("릭").getId();

            //when & then
            assertThatThrownBy(() -> teamService.deleteById(1000L, memberId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("이미 삭제된 팀을 삭제하는 경우 팀이 존재하지 않는다는 예외를 던진다.")
        void delete_alreadyDeleted_exception() {
            //given
            final Member member = saveMember("릭");
            final Team team = saveTeam(member);
            final Long teamId = team.getId();
            final Long memberId = member.getId();
            teamService.deleteById(teamId, memberId);
            entityManager.flush();
            entityManager.clear();

            //when & then
            assertThatThrownBy(() -> teamService.deleteById(teamId, memberId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
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
            final List<TeamDto> teams = teamService.findAllByMemberId(roma.getId()).getTeams();

            // then
            assertThat(teams).hasSize(2);
        }

        @Test
        @DisplayName("주어진 memberId의 멤버가 존재하지 않을 때 예외를 던진다.")
        void findAllByMemberId_memberNotFound_exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findAllByMemberId(100_000L))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않습니다.", String.valueOf(100_000L));
        }
    }
}
