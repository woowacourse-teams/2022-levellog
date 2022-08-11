package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.InterviewRole;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamStatus;
import com.woowacourse.levellog.team.dto.InterviewRoleDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamAndRoleDto;
import com.woowacourse.levellog.team.dto.TeamAndRolesDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import com.woowacourse.levellog.team.exception.DuplicateParticipantsException;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.exception.TeamTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TeamService의")
class TeamServiceTest extends ServiceTest {

    private Member saveAndGetMember(final String nickname) {
        return memberRepository.save(new Member(nickname, (int) System.nanoTime(), "profile.png"));
    }

    private Team saveAndGetTeam(final String title, final int interviewerNumber, final LocalDateTime startAt) {
        return teamRepository.save(
                new Team(title, "피니시방", startAt, "jason.png", interviewerNumber));
    }

    private List<Long> saveAllParticipant(final Team team, final Member host, final Member... participants) {
        final List<Long> savedParticipantsMemberIds = new ArrayList<>();
        final Participant hostParticipant = participantRepository.save(new Participant(team, host, true));
        savedParticipantsMemberIds.add(hostParticipant.getMember().getId());
        for (final Member participant : participants) {
            final Participant savedParticipant = participantRepository.save(new Participant(team, participant, false));
            savedParticipantsMemberIds.add(savedParticipant.getMember().getId());
        }
        return savedParticipantsMemberIds;
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("전체 팀 목록을 조회한다.")
        void findAll() {
            //given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Member member3 = saveAndGetMember("로마");

            final Team team1 = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            final Team team2 = saveAndGetTeam("선릉 브라운조", 1, LocalDateTime.now().plusDays(6));

            participantRepository.save(new Participant(team1, member2, true));
            participantRepository.save(new Participant(team1, member3, false));
            participantRepository.save(new Participant(team2, member1, true));
            participantRepository.save(new Participant(team2, member2, false));

            team2.close(LocalDateTime.now().plusDays(7));

            //when
            final TeamAndRolesDto response = teamService.findAll(member1.getId());

            //then
            final List<String> actualTitles = response.getTeams()
                    .stream()
                    .map(TeamAndRoleDto::getTitle)
                    .collect(Collectors.toList());

            final List<Long> actualHostIds = response.getTeams()
                    .stream()
                    .map(TeamAndRoleDto::getHostId)
                    .collect(Collectors.toList());

            final List<Integer> actualParticipantSizes = response.getTeams()
                    .stream()
                    .map(TeamAndRoleDto::getParticipants)
                    .map(List::size)
                    .collect(Collectors.toList());

            final List<TeamStatus> actualCloseStatuses = response.getTeams()
                    .stream()
                    .map(TeamAndRoleDto::getStatus)
                    .collect(Collectors.toList());

            final List<Boolean> actualIsParticipants = response.getTeams()
                    .stream().map(TeamAndRoleDto::getIsParticipant)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(actualTitles).contains(team1.getTitle(), team2.getTitle()),
                    () -> assertThat(actualHostIds).contains(member1.getId(), member2.getId()),
                    () -> assertThat(actualParticipantSizes).contains(2, 2),
                    () -> assertThat(actualCloseStatuses).containsExactly(TeamStatus.READY, TeamStatus.CLOSED),
                    () -> assertThat(actualIsParticipants).containsExactly(false, true),
                    () -> assertThat(response.getTeams()).hasSize(2)
            );
        }

        @Test
        @DisplayName("삭제된 팀을 제외한 팀 목록을 조회한다.")
        void findAll_exceptDeleted() {
            //given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Member member3 = saveAndGetMember("로마");

            final Team team1 = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            final Team team2 = saveAndGetTeam("선릉 브라운조", 1, LocalDateTime.now().plusDays(6));

            participantRepository.save(new Participant(team1, member2, true));
            participantRepository.save(new Participant(team1, member3, false));
            participantRepository.save(new Participant(team2, member1, true));
            participantRepository.save(new Participant(team2, member2, false));

            team2.delete(LocalDateTime.now());

            //when
            final TeamAndRolesDto response = teamService.findAll(member1.getId());

            //then
            assertThat(response.getTeams()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("팀을 생성한다.")
        void save() {
            //given
            final Long participant1 = memberRepository.save(new Member("알린", 1111, "alien.png")).getId();
            final Long participant2 = memberRepository.save(new Member("페퍼", 2222, "pepper.png")).getId();
            final Long participant3 = memberRepository.save(new Member("로마", 3333, "roma.png")).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 2, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(List.of(participant2, participant3)));

            //when
            final Long id = teamService.save(teamDto, participant1);

            //then
            final Optional<Team> team = teamRepository.findById(id);
            assertThat(team).isPresent();
        }

        @Test
        @DisplayName("참가자가 중복되면 예외가 발생한다.")
        void save_duplicate_exceptionThrown() {
            //given
            final Long participant1 = memberRepository.save(new Member("알린", 1111, "alien.png")).getId();
            final Long participant2 = memberRepository.save(new Member("페퍼", 2222, "pepper.png")).getId();
            final Long participant3 = memberRepository.save(new Member("로마", 3333, "roma.png")).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(List.of(participant1, participant2, participant3)));

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, participant1))
                    .isInstanceOf(DuplicateParticipantsException.class)
                    .hasMessageContaining("참가자 중복");
        }

        @Test
        @DisplayName("호스트 이외의 참가자가 없으면 예외가 발생한다.")
        void save_noParticipant_exceptionThrown() {
            //given
            final Long alienId = memberRepository.save(new Member("알린", 1111, "alien.png")).getId();
            final TeamWriteDto teamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(Collections.emptyList()));

            //when & then
            assertThatThrownBy(() -> teamService.save(teamDto, alienId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("호스트 이외의 참가자가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("findMyRole 메서드는")
    class FindMyRole {

        @Test
        @DisplayName("팀의 참가자에 대한 나의 역할을 조회한다. - interviewer")
        void success_interviewer() {
            // given
            final Team team = saveAndGetTeam("레벨로그 모의 인터뷰", 1, LocalDateTime.now().plusDays(6));

            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("해리");
            final Member member3 = saveAndGetMember("알린");

            saveAllParticipant(team, member1, member2, member3);

            final Long teamId = team.getId();
            final Long targetMemberId = member1.getId();
            final Long requestMemberId = member2.getId();

            // when
            final InterviewRoleDto actual = teamService.findMyRole(teamId, targetMemberId, requestMemberId);

            // then
            assertThat(actual.getMyRole()).isEqualTo(InterviewRole.INTERVIEWER);
        }

        @Test
        @DisplayName("팀의 참가자에 대한 나의 역할을 조회한다. - observer")
        void success_observer() {
            // given
            final Team team = saveAndGetTeam("레벨로그 모의 인터뷰", 1, LocalDateTime.now().plusDays(6));

            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("해리");
            final Member member3 = saveAndGetMember("알린");

            saveAllParticipant(team, member1, member2, member3);

            final Long teamId = team.getId();
            final Long targetMemberId = member1.getId();
            final Long requestMemberId = member3.getId();

            // when
            final InterviewRoleDto actual = teamService.findMyRole(teamId, targetMemberId, requestMemberId);

            // then
            assertThat(actual.getMyRole()).isEqualTo(InterviewRole.OBSERVER);
        }

        @Test
        @DisplayName("팀의 참가자가 아닌 member가 요청하면 예외를 던진다.")
        void iAmNotParticipant_exceptionThrown() {
            // given
            final Team team = saveAndGetTeam("레벨로그 모의 인터뷰", 1, LocalDateTime.now().plusDays(6));

            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("해리");
            final Member member3 = saveAndGetMember("알린");

            saveAllParticipant(team, member1, member2);

            final Long teamId = team.getId();
            final Long targetMemberId = member1.getId();
            final Long requestMemberId = member3.getId();

            // when & then
            assertThatThrownBy(() -> teamService.findMyRole(teamId, targetMemberId, requestMemberId))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("targetMember가 팀의 참가자가 아니면 예외를 던진다.")
        void targetNotParticipant_exceptionThrown() {
            // given
            final Team team = saveAndGetTeam("레벨로그 모의 인터뷰", 1, LocalDateTime.now().plusDays(6));

            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("해리");
            final Member member3 = saveAndGetMember("알린");

            saveAllParticipant(team, member1, member2);

            final Long teamId = team.getId();
            final Long targetMemberId = member3.getId();
            final Long requestMemberId = member1.getId();

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
        void findByTeamIdAndMemberId() {
            //given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 2, LocalDateTime.now().plusDays(6));

            participantRepository.save(new Participant(team, member1, true));
            participantRepository.save(new Participant(team, member2, false));

            //when
            final TeamAndRoleDto response = teamService.findByTeamIdAndMemberId(team.getId(), member1.getId());

            //then
            assertAll(
                    () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                    () -> assertThat(response.getHostId()).isEqualTo(member1.getId()),
                    () -> assertThat(response.getStatus()).isEqualTo(TeamStatus.READY),
                    () -> assertThat(response.getParticipants()).hasSize(2)
            );
        }

        @Test
        @DisplayName("없는 id에 해당하는 팀을 조회하면 예외를 던진다.")
        void teamNotFound_Exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findByTeamIdAndMemberId(1000L, 1L))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }

        @Nested
        @DisplayName("요청한 유저가 팀에 참가자일 때")
        class ParticipantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이, isParticipant를 true로 응답한다.")
            void findByTeamIdAndMemberId() {
                //given
                final Member rick = saveAndGetMember("릭");
                final Member pepper = saveAndGetMember("페퍼");
                final Member roma = saveAndGetMember("로마");
                final Member alien = saveAndGetMember("알린");
                final Member eve = saveAndGetMember("이브");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2, LocalDateTime.now().plusDays(6));

                saveAllParticipant(team, rick, pepper, roma, alien, eve);

                //when
                final TeamAndRoleDto responseOfPepper = teamService.findByTeamIdAndMemberId(team.getId(),
                        pepper.getId());
                final TeamAndRoleDto responseOfEve = teamService.findByTeamIdAndMemberId(team.getId(),
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
            void findByTeamIdAndMemberId_manyInterviewerNumber() {
                //given
                final Member rick = saveAndGetMember("릭");
                final Member pepper = saveAndGetMember("페퍼");
                final Member roma = saveAndGetMember("로마");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2, LocalDateTime.now().plusDays(6));

                saveAllParticipant(team, rick, pepper, roma);

                //when
                final TeamAndRoleDto response = teamService.findByTeamIdAndMemberId(team.getId(), pepper.getId());

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
        @DisplayName("요청한 유저가 팀에 참가자가 아닐 때")
        class NotParticipantRequest {

            @Test
            @DisplayName("인터뷰어와 인터뷰이가 빈 상태이고 isParticipant를 false로 응답한다.")
            void findByTeamIdAndMemberId() {
                //given
                final Member member1 = saveAndGetMember("릭");
                final Member member2 = saveAndGetMember("페퍼");
                final Member member3 = saveAndGetMember("로마");
                final Member member4 = saveAndGetMember("알린");
                final Team team = saveAndGetTeam("잠실 제이슨조", 2, LocalDateTime.now().plusDays(6));

                saveAllParticipant(team, member1, member2, member3);

                //when
                final TeamAndRoleDto response = teamService.findByTeamIdAndMemberId(team.getId(),
                        member4.getId());

                //then
                assertAll(
                        () -> assertThat(response.getTitle()).isEqualTo(team.getTitle()),
                        () -> assertThat(response.getHostId()).isEqualTo(member1.getId()),
                        () -> assertThat(response.getParticipants()).hasSize(3),
                        () -> assertThat(response.getInterviewers()).isEmpty(),
                        () -> assertThat(response.getInterviewees()).isEmpty(),
                        () -> assertThat(response.getIsParticipant()).isFalse()
                );
            }
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("id에 해당하는 팀 정보를 변경한다.")
        void success() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Member member3 = saveAndGetMember("이브");

            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            final List<Long> savedParticipantsMemberIds = saveAllParticipant(team, member1, member2, member3);
            final TeamWriteDto request = new TeamWriteDto("잠실 준조", "트랙룸", 2, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(savedParticipantsMemberIds));

            // when
            teamService.update(request, team.getId(), member1.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertAll(
                    () -> assertThat(actualTeam.getTitle()).isEqualTo(request.getTitle()),
                    () -> assertThat(actualTeam.getPlace()).isEqualTo(request.getPlace()),
                    () -> assertThat(actualTeam.getStartAt()).isEqualTo(request.getStartAt()),
                    () -> assertThat(actualTeam.getInterviewerNumber()).isEqualTo(request.getInterviewerNumber())
            );
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 수정하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            final List<Long> participantsMemberIds = saveAllParticipant(team, member1, member2);

            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(participantsMemberIds));

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
            final Long memberId = saveAndGetMember("릭").getId();
            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(Collections.emptyList()));

            //when & then
            assertThatThrownBy(() -> teamService.update(request, 1000L, memberId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }

        @Test
        @DisplayName("인터뷰 시작 이후에 팀을 수정하려고 하면 예외를 던진다.")
        void updateAfterStartAt_Exception() {
            //given
            final Member member = saveAndGetMember("릭");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(3));
            final List<Long> participantsMemberIds = saveAllParticipant(team, member);
            final TeamWriteDto request = new TeamWriteDto("잠실 네오조", "트랙룸", 1, LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(participantsMemberIds));

            //when & then
            final Long teamId = team.getId();
            final Long memberId = member.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(TeamTimeException.class)
                    .hasMessageContaining("인터뷰가 시작된 이후에는 수정할 수 없습니다.", teamId, team.getStartAt());
        }
    }

    @Nested
    @DisplayName("close 메서드는")
    class Close {

        @Test
        @DisplayName("입력 받은 팀의 인터뷰를 종료한다.")
        void close() {
            // given
            final Member rick = saveAndGetMember("릭");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(3));
            participantRepository.save(new Participant(team, rick, true));

            // when
            teamService.close(team.getId(), rick.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(actualTeam.isClosed()).isTrue();
        }

        @Test
        @DisplayName("호스트가 아닌 사용자가 인터뷰를 종료하면 예외가 발생한다.")
        void close_notHost_exceptionThrown() {
            // given
            final Member rick = saveAndGetMember("릭");
            final Member alien = saveAndGetMember("알린");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            participantRepository.save(new Participant(team, rick, true));
            participantRepository.save(new Participant(team, alien, false));

            // when & then
            assertThatThrownBy(() -> teamService.close(team.getId(), alien.getId()))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContainingAll("호스트 권한이 없습니다.", alien.getId().toString());
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Delete {

        @Test
        @DisplayName("id에 해당하는 팀을 deleted 상태로 만든다.")
        void success() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));

            saveAllParticipant(team, member1, member2);

            // when
            teamService.deleteById(team.getId(), member1.getId());

            // then
            final Optional<Team> actual = teamRepository.findById(team.getId());
            assertThat(actual).isPresent();
            assertTrue(actual.get().isDeleted());
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 삭제하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = saveAndGetMember("릭");
            final Member member2 = saveAndGetMember("페퍼");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));

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
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            saveAllParticipant(team, member);

            //when & then
            assertThatThrownBy(() -> teamService.deleteById(1000L, member.getId()))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContaining("팀이 존재하지 않습니다. 입력한 팀 id : [1000]");
        }

        @Test
        @DisplayName("이미 삭제된 팀을 삭제하는 경우 호스트가 존재하지 않는다는 예외를 던진다.")
        void alreadyDeleted_Exception() {
            //given
            final Member member = saveAndGetMember("릭");
            final Team team = saveAndGetTeam("잠실 제이슨조", 1, LocalDateTime.now().plusDays(6));
            saveAllParticipant(team, member);
            final Long teamId = team.getId();
            final Long memberId = member.getId();
            teamService.deleteById(teamId, memberId);

            //when & then
            assertThatThrownBy(() -> teamService.deleteById(teamId, memberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContaining("모든 참가자 중 호스트가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("findAllByMemberId 메서드는 ")
    class FindAllByMemberId {

        @Test
        @DisplayName("주어진 memberId의 멤버가 참가한 모든 팀을 조회한다.")
        void success() {
            // given
            final Member roma = saveAndGetMember("로마");
            final Member harry = saveAndGetMember("해리");
            final Member alien = saveAndGetMember("알린");

            final TeamWriteDto romaTeamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1,
                    LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(List.of(harry.getId())));
            final TeamWriteDto romaTeamDto2 = new TeamWriteDto("잠실 준조", "트랙룸", 1,
                    LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(List.of(harry.getId(), alien.getId())));

            final TeamWriteDto harryTeamDto = new TeamWriteDto("잠실 준조", "트랙룸", 1,
                    LocalDateTime.now().plusDays(6),
                    new ParticipantIdsDto(List.of(alien.getId())));

            teamService.save(romaTeamDto, roma.getId());
            teamService.save(romaTeamDto2, roma.getId());
            teamService.save(harryTeamDto, harry.getId());

            // when
            final List<TeamDto> teams = teamService.findAllByMemberId(roma.getId()).getTeams();

            // then
            assertThat(teams).hasSize(2);
        }

        @Test
        @DisplayName("주어진 memberId의 멤버가 존재하지 않을 때 예외를 던진다.")
        void memberNotFound_exception() {
            // when & then
            assertThatThrownBy(() -> teamService.findAllByMemberId(100_000L))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않음", String.valueOf(100_000L));
        }
    }
}
