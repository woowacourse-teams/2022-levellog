package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static com.woowacourse.levellog.team.domain.TeamStatus.CLOSED;
import static com.woowacourse.levellog.team.domain.TeamStatus.IN_PROGRESS;
import static com.woowacourse.levellog.team.domain.TeamStatus.READY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import com.woowacourse.levellog.team.dto.request.TeamWriteRequest;
import com.woowacourse.levellog.team.dto.response.InterviewRoleResponse;
import com.woowacourse.levellog.team.dto.response.TeamStatusResponse;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamService의")
class TeamServiceTest extends ServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

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

            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 2, TEAM_START_TIME,
                    List.of(alien, pepper, roma), Collections.emptyList());
            //when
            final Long id = teamService.save(request, LoginStatus.fromLogin(alien));

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

            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper, roma, roma), Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> teamService.save(request, LoginStatus.fromLogin(alien)))
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

            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper), List.of(roma, roma));

            //when & then
            assertThatThrownBy(() -> teamService.save(request, LoginStatus.fromLogin(alien)))
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

            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien, pepper, roma), List.of(roma));

            //when & then
            assertThatThrownBy(() -> teamService.save(request, LoginStatus.fromLogin(alien)))
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

            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper, roma), List.of(rick));

            //when & then
            assertThatThrownBy(() -> teamService.save(request, LoginStatus.fromLogin(alien)))
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

            // when
            final InterviewRoleResponse actual = teamService.findMyRole(teamId, targetMemberId, getLoginStatus(harry));

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

            // when
            final InterviewRoleResponse actual = teamService.findMyRole(teamId, targetMemberId, getLoginStatus(alien));

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

            // when & then
            assertThatThrownBy(() -> teamService.findMyRole(teamId, targetMemberId, getLoginStatus(alien)))
                    .isInstanceOf(NotParticipantException.class);
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

            // when & then
            assertThatThrownBy(() -> teamService.findMyRole(teamId, targetMemberId, getLoginStatus(rick)))
                    .isInstanceOf(NotParticipantException.class);
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
            final TeamStatusResponse actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(READY);
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
            final TeamStatusResponse actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(IN_PROGRESS);
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
            final TeamStatusResponse actual = teamService.findStatus(team.getId());

            // then
            assertThat(actual.getStatus()).isEqualTo(CLOSED);
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
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 2, AFTER_START_TIME,
                    participantsIds, Collections.emptyList());
            entityManager.clear();

            // when
            teamService.update(request, team.getId(), getLoginStatus(rick));

            // then
            final Team actualTeam = teamRepository.getTeam(team.getId());
            final TeamDetail detail = actualTeam.getDetail();

            final List<Participant> values = actualTeam.getParticipants().getValues();

            assertAll(
                    () -> assertThat(detail.getTitle()).isEqualTo(request.getTitle()),
                    () -> assertThat(detail.getPlace()).isEqualTo(request.getPlace()),
                    () -> assertThat(detail.getStartAt()).isEqualTo(request.getStartAt()),
                    () -> assertThat(actualTeam.getInterviewerNumber()).isEqualTo(request.getInterviewerNumber()),
                    () -> assertThat(values).extracting("memberId")
                            .contains(rick.getId(), eve.getId(), alien.getId(), roma.getId())
            );
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 수정하는 경우 예외를 던진다.")
        void update_hostUnauthorized_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(rick, pepper);

            final TeamWriteRequest request = new TeamWriteRequest("잠실 네오조", "트랙룸", 1, AFTER_START_TIME,
                    List.of(rick.getId()), Collections.emptyList());

            // when, then
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(pepper)))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void update_teamNotFound_exception() {
            //given
            final Member rick = saveMember("릭");
            final TeamWriteRequest request = new TeamWriteRequest("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    Collections.emptyList(), Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> teamService.update(request, 1000L, getLoginStatus(rick)))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 팀을 수정하려고 하면 예외를 던진다.")
        void update_notReady_exception() {
            //given
            final Member rick = saveMember("릭");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(rick, roma);
            final TeamWriteRequest request = new TeamWriteRequest("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(rick.getId(), roma.getId()), Collections.emptyList());
            timeStandard.setInProgress();

            //when & then
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(rick)))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.", teamId, team.getDetail().getStartAt());
        }

        @Test
        @DisplayName("참가자가 중복되면 예외가 발생한다.")
        void update_duplicate_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteRequest request = new TeamWriteRequest("잠실 네오조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper.getId(), pepper.getId()), Collections.emptyList());

            //when & then
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(alien)))
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
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien.getId(), pepper.getId()), List.of(roma.getId(), roma.getId()));

            //when & then
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(alien)))
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
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(alien.getId(), pepper.getId(), roma.getId()), List.of(roma.getId()));

            //when & then
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(alien)))
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
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 1, TEAM_START_TIME,
                    List.of(pepper.getId(), roma.getId()), List.of(rick.getId()));

            //when & then
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(alien)))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.");
        }

        @Test
        @DisplayName("수정하려는 팀의 참가자 수가 인터뷰어 수보다 많지 않으면 예외가 발생한다.")
        void update_participantsSizeLessThanInterviewerNumber_exception() {
            //given
            final Member alien = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");
            final Member rick = saveMember("릭");

            final Long teamId = saveTeam(alien, pepper, roma).getId();
            final TeamWriteRequest request = new TeamWriteRequest("잠실 준조", "트랙룸", 3, TEAM_START_TIME,
                    List.of(pepper.getId(), roma.getId()), List.of(alien.getId(), rick.getId()));

            //when & then
            assertThatThrownBy(() -> teamService.update(request, teamId, getLoginStatus(alien)))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자 수는 인터뷰어 수 보다 많아야 합니다.");
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
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            timeStandard.setInProgress();

            // when
            teamService.close(team.getId(), getLoginStatus(rick));

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
            assertThatThrownBy(() -> teamService.close(teamId, getLoginStatus(alien)))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContainingAll("호스트 권한이 없습니다.", String.valueOf(alien.getId()));
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
            teamService.delete(team.getId(), getLoginStatus(rick));
            entityManager.flush();
            entityManager.clear();

            // then
            assertThatThrownBy(() -> teamRepository.getTeam(team.getId()))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 삭제하는 경우 예외를 던진다.")
        void delete_hostUnauthorized_exception() {
            // given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            // when, then
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.delete(teamId, getLoginStatus(pepper)))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void delete_teamNotFound_exception() {
            //given
            final Member rick = saveMember("릭");

            //when & then
            assertThatThrownBy(() -> teamService.delete(1000L, getLoginStatus(rick)))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("이미 삭제된 팀을 삭제하는 경우 팀이 존재하지 않는다는 예외를 던진다.")
        void delete_alreadyDeleted_exception() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(rick, pepper);

            final Long teamId = team.getId();
            teamService.delete(teamId, getLoginStatus(rick));
            entityManager.flush();
            entityManager.clear();

            //when & then
            assertThatThrownBy(() -> teamService.delete(teamId, getLoginStatus(rick)))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다.");
        }
    }
}
