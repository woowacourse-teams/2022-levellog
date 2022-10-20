package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static com.woowacourse.levellog.fixture.TimeFixture.BEFORE_START_TIME;
import static com.woowacourse.levellog.team.domain.TeamStatus.CLOSED;
import static com.woowacourse.levellog.team.domain.TeamStatus.READY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamFilterCondition;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponse;
import com.woowacourse.levellog.team.dto.response.TeamListResponses;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamQueryService의")
public class TeamQueryServiceTest extends ServiceTest {

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("진행중인 팀 목록을 최근 생성일 순으로 정렬하여 조회한다.")
        void findAll_openTeam_success() {
            //given
            final Member rick = saveMember("릭");
            final Member pepper = saveMember("페퍼");
            final Member roma = saveMember("로마");

            final Team pepperTeam = saveTeam(pepper, roma);
            final Team rickTeam = saveTeam(rick, pepper);
            final Team romaTeam = saveTeam(roma, rick);

            rickTeam.close(AFTER_START_TIME);
            entityManager.flush();

            //when
            final TeamListResponses response = teamQueryService.findAll(TeamFilterCondition.OPEN, 0, 10);

            //then
            assertThat(response.getTeams()).hasSize(2)
                    .extracting(TeamListResponse::getId, TeamListResponse::getStatus)
                    .containsExactly(
                            tuple(romaTeam.getId(), READY),
                            tuple(pepperTeam.getId(), READY)
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

            entityManager.flush();

            //when
            final TeamListResponses response = teamQueryService.findAll(TeamFilterCondition.CLOSE, 0, 10);

            //then
            assertThat(response.getTeams()).hasSize(2)
                    .extracting(TeamListResponse::getId, TeamListResponse::getStatus)
                    .containsExactly(
                            tuple(eveTeam.getId(), CLOSED),
                            tuple(rickTeam.getId(), CLOSED)
                    );
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
            entityManager.flush();

            //when
            final TeamListResponses response = teamQueryService.findAll(TeamFilterCondition.OPEN, 0, 10);

            //then
            assertThat(response.getTeams()).hasSize(1);
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
            final TeamDetailResponse response = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                    getLoginStatus(rick));

            //then
            assertAll(
                    () -> assertThat(response.getTitle()).isEqualTo(team.getDetail().getTitle()),
                    () -> assertThat(response.getHostId()).isEqualTo(rick.getId()),
                    () -> assertThat(response.getStatus()).isEqualTo(READY),
                    () -> assertThat(response.getParticipants()).hasSize(2)
            );
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 조회하면 예외를 던진다.")
        void findByTeamIdAndMemberId_notFound_exception() {
            // when & then
            assertThatThrownBy(() -> teamQueryService.findByTeamIdAndMemberId(1000L, LoginStatus.fromLogin(1L)))
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
                final TeamDetailResponse responseOfPepper = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                        getLoginStatus(pepper));
                final TeamDetailResponse responseOfEve = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                        getLoginStatus(eve));

                //then
                assertAll(
                        () -> assertThat(responseOfPepper.getTitle()).isEqualTo(team.getDetail().getTitle()),
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
                final TeamDetailResponse response = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                        getLoginStatus(pepper));

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getDetail().getTitle()),
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
                final TeamDetailResponse response = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                        getLoginStatus(pobi));

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getDetail().getTitle()),
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
                final TeamDetailResponse response = teamQueryService.findByTeamIdAndMemberId(team.getId(),
                        getLoginStatus(alien));

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getDetail().getTitle()),
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
            final List<TeamListResponse> teams = teamQueryService.findAllByMemberId(getLoginStatus(roma))
                    .getTeams();

            // then
            assertThat(teams).hasSize(2);
        }
    }
}
