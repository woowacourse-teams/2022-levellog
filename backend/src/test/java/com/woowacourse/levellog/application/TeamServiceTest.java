package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamAndRoleDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamService의")
class TeamServiceTest extends ServiceTest {

    @Test
    @DisplayName("save 메서드는 팀을 생성한다.")
    void save() {
        //given
        final Long participant1 = memberRepository.save(new Member("알린", 1111, "alien.png")).getId();
        final Long participant2 = memberRepository.save(new Member("페퍼", 2222, "pepper.png")).getId();
        final Long participant3 = memberRepository.save(new Member("로마", 3333, "roma.png")).getId();
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 준조", "트랙룸", 2, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(participant1, participant2, participant3)));

        //when
        final Long id = teamService.save(teamCreateDto, participant1);

        //then
        final Optional<Team> possibleTeam = teamRepository.findById(id);
        assertThat(possibleTeam).isPresent();

        final Team team = possibleTeam.get();
        assertAll(
                () -> assertThat(team.getTitle()).isEqualTo("잠실 준조"),
                () -> assertThat(team.getPlace()).isEqualTo("트랙룸"),
                () -> assertThat(team.getInterviewerNumber()).isEqualTo(2),
                () -> assertThat(team.isClosed()).isFalse()
        );
    }

    @Test
    @DisplayName("findAll 메서드는 전체 팀 목록을 조회한다.")
    void findAll() {
        //given
        final Member member1 = saveAndGetMember("릭");
        final Member member2 = saveAndGetMember("페퍼");

        final Team team1 = saveAndGetTeam("잠실 제이슨조", 1);
        final Team team2 = saveAndGetTeam("선릉 브라운조", 1);

        participantRepository.save(new Participant(team1, member1, true));
        participantRepository.save(new Participant(team1, member2, false));
        participantRepository.save(new Participant(team2, member1, false));
        participantRepository.save(new Participant(team2, member2, true));

        //when
        final TeamsDto response = teamService.findAll();

        //then
        final List<String> actualTitles = response.getTeams()
                .stream()
                .map(TeamDto::getTitle)
                .collect(Collectors.toList());

        final List<Long> actualHostIds = response.getTeams()
                .stream()
                .map(TeamDto::getHostId)
                .collect(Collectors.toList());

        final List<Integer> actualParticipantSizes = response.getTeams()
                .stream()
                .map(TeamDto::getParticipants)
                .map(List::size)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(actualTitles).contains(team1.getTitle(), team2.getTitle()),
                () -> assertThat(actualHostIds).contains(member1.getId(), member2.getId()),
                () -> assertThat(actualParticipantSizes).contains(2, 2),
                () -> assertThat(response.getTeams()).hasSize(2)
        );
    }

    private Member saveAndGetMember(final String nickname) {
        return memberRepository.save(new Member(nickname, (int) System.nanoTime(), "profile.png"));
    }

    private Team saveAndGetTeam(final String title, final int interviewerNumber) {
        return teamRepository.save(
                new Team(title, "피니시방", LocalDateTime.now().plusDays(3), "jason.png", interviewerNumber));
    }

    private void saveAllParticipant(final Team team, final Member host, final Member... participants) {
        participantRepository.save(new Participant(team, host, true));
        for (final Member participant : participants) {
            participantRepository.save(new Participant(team, participant, false));
        }
    }

    // TODO: 2022/07/31 findByIdWithRole 메서드 테스트로 교체
    @Nested
    @DisplayName("findById 메서드는")
    class findById {

        @Test
        @DisplayName("id에 해당하는 팀을 조회한다.")
        void findById() {
            //given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);

            saveAllParticipant(team, member1, member2);

            //when
            final TeamDto response = teamService.findById(team.getId());

            //then
            assertAll(
                    () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                    () -> assertThat(response.getHostId()).isEqualTo(member1.getId()),
                    () -> assertThat(response.getParticipants()).hasSize(2)
            );
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 조회하면 예외를 던진다.")
        void teamNotFound_Exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findById(1000L))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }
    }

    @Nested
    @DisplayName("findByIdWithRole 메서드는")
    class findByIdWithRole {

        @Test
        @DisplayName("없는 id에 해당하는 팀을 조회하면 예외를 던진다.")
        void teamNotFound_Exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findByTeamIdAndRequestUserIdWithRole(1000L, 1L))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }

        @Nested
        @DisplayName("요청한 유저가 팀에 참가자일 때")
        class participantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이를 포함해서 응답한다.")
            void findById() {
                //given
                final Member rick = saveAndGetMember("릭");
                final Member pepper = saveAndGetMember("페퍼");
                final Member roma = saveAndGetMember("로마");
                final Member alien = saveAndGetMember("알린");
                final Member eve = saveAndGetMember("이브");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2);

                saveAllParticipant(team, rick, pepper, roma, alien, eve);

                //when
                final TeamAndRoleDto responseOfPepper = teamService.findByTeamIdAndRequestUserIdWithRole(team.getId(),
                        pepper.getId());
                final TeamAndRoleDto responseOfEve = teamService.findByTeamIdAndRequestUserIdWithRole(team.getId(),
                        eve.getId());

                //then
                assertAll(
                        () -> assertThat(responseOfPepper.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(responseOfPepper.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(responseOfPepper.getParticipants()).hasSize(5),

                        () -> assertThat(responseOfPepper.getInterviewers()).containsExactly(roma.getId(),
                                alien.getId()),
                        () -> assertThat(responseOfPepper.getInterviewees()).containsExactly(rick.getId(), eve.getId()),

                        () -> assertThat(responseOfEve.getInterviewers()).containsExactly(rick.getId(), pepper.getId()),
                        () -> assertThat(responseOfEve.getInterviewees()).containsExactly(alien.getId(), roma.getId())
                );
            }

            @Test
            @DisplayName("참가자가 3명이고, 인터뷰어 수는 2명이면 인터뷰어와 인터뷰이를 포함해서 응답한다.")
            void findById_manyInterviewerNumber() {
                //given
                final Member rick = saveAndGetMember("릭");
                final Member pepper = saveAndGetMember("페퍼");
                final Member roma = saveAndGetMember("로마");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2);

                saveAllParticipant(team, rick, pepper, roma);

                //when
                final TeamAndRoleDto responseOfPepper = teamService.findByTeamIdAndRequestUserIdWithRole(team.getId(),
                        pepper.getId());

                //then
                assertAll(
                        () -> assertThat(responseOfPepper.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(responseOfPepper.getHostId()).isEqualTo(rick.getId()),
                        () -> assertThat(responseOfPepper.getParticipants()).hasSize(3),

                        () -> assertThat(responseOfPepper.getInterviewers()).containsExactly(roma.getId(), rick.getId()),
                        () -> assertThat(responseOfPepper.getInterviewees()).containsExactly(rick.getId(), roma.getId())
                );
            }
        }

        @Nested
        @DisplayName("요청한 유저가 팀에 참가자가 아닐 때")
        class notParticipantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이가 빈 상태로 응답한다.")
            void findById() {
                //given
                final Member member1 = saveAndGetMember("릭");
                final Member member2 = saveAndGetMember("페퍼");
                final Member member3 = saveAndGetMember("로마");
                final Member member4 = saveAndGetMember("알린");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2);

                saveAllParticipant(team, member1, member2, member3);

                //when
                final TeamAndRoleDto response = teamService.findByTeamIdAndRequestUserIdWithRole(team.getId(),
                        member4.getId());

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(response.getHostId()).isEqualTo(member1.getId()),
                        () -> assertThat(response.getParticipants()).hasSize(3),
                        () -> assertThat(response.getInterviewers()).isEmpty(),
                        () -> assertThat(response.getInterviewees()).isEmpty()
                );
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("id에 해당하는 팀 정보를 변경한다.")
        void success() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);

            saveAllParticipant(team, member1, member2);

            final TeamUpdateDto request = new TeamUpdateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3));

            // when
            teamService.update(request, team.getId(), member1.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(actualTeam.getTitle()).isEqualTo(request.getTitle()),
                    () -> assertThat(actualTeam.getPlace()).isEqualTo(request.getPlace()),
                    () -> assertThat(actualTeam.getStartAt()).isEqualTo(request.getStartAt())
            );
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 수정하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);

            saveAllParticipant(team, member1, member2);

            final TeamUpdateDto request = new TeamUpdateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3));

            // when, then
            final Long memberId = member2.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void teamNotFound_Exception() {
            //given
            final TeamUpdateDto request = new TeamUpdateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3));
            final Member member = saveAndGetMember("릭");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);
            saveAllParticipant(team, member);

            //when & then
            assertThatThrownBy(() -> teamService.update(request, 1000L, member.getId()))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("delete 메서드는 id에 해당하는 팀을 삭제한다.")
        void success() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);

            saveAllParticipant(team, member1, member2);

            // when
            teamService.deleteById(team.getId(), member1.getId());

            // then
            assertTrue(teamRepository.findById(team.getId()).isEmpty());
        }

        @Test
        @DisplayName("delete 메서드는 호스트가 아닌 멤버가 팀을 삭제하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);

            saveAllParticipant(team, member1, member2);

            // when, then
            final Long memberId = member2.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.deleteById(teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 수정하면 예외를 던진다.")
        void teamNotFound_Exception() {
            //given
            final Member member = saveAndGetMember("릭");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1);
            saveAllParticipant(team, member);

            //when & then
            assertThatThrownBy(() -> teamService.deleteById(1000L, member.getId()))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }
    }
}
