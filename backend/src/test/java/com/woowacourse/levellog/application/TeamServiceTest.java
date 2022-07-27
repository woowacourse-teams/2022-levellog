package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import com.woowacourse.levellog.team.dto.TeamsDto;
import com.woowacourse.levellog.team.exception.HostUnauthorizedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("TeamService의")
class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save 메서드는 팀을 생성한다.")
    void save() {
        //given
        final Long participant1 = memberRepository.save(new Member("알린", 1111, "alien.png")).getId();
        final Long participant2 = memberRepository.save(new Member("페퍼", 2222, "pepper.png")).getId();
        final Long participant3 = memberRepository.save(new Member("로마", 3333, "roma.png")).getId();
        final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 준조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(participant1, participant2, participant3)));

        //when
        final Long id = teamService.save(teamCreateDto, participant1);

        //then
        final Optional<Team> team = teamRepository.findById(id);
        assertThat(team).isPresent();
    }

    @Test
    @DisplayName("findAll 메서드는 전체 팀 목록을 조회한다.")
    void findAll() {
        //given
        final Member member1 = getMember("릭");
        final Member member2 = getMember("페퍼");

        final Team team1 = getTeam("잠실 제이슨조");
        final Team team2 = getTeam("선릉 브라운조");

        participantRepository.save(new Participant(team1, member1, true));
        participantRepository.save(new Participant(team1, member2, false));
        participantRepository.save(new Participant(team2, member1, false));
        participantRepository.save(new Participant(team2, member2, true));

        //when
        final TeamsDto response = teamService.findAll();

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

        //then
        assertAll(
                () -> assertThat(actualTitles).containsExactly(team1.getTitle(), team2.getTitle()),
                () -> assertThat(actualHostIds).containsExactly(member1.getId(), member2.getId()),
                () -> assertThat(actualParticipantSizes).containsExactly(2, 2),
                () -> assertThat(response.getTeams()).hasSize(2)
        );
    }

    @Test
    @DisplayName("findById 메서드는 id에 해당하는 팀을 조회한다.")
    void findById() {
        //given
        final Member member1 = getMember("릭");
        final Member member2 = getMember("페퍼");
        final Team team = getTeam("잠실 제이슨조");

        participantRepository.save(new Participant(team, member1, true));
        participantRepository.save(new Participant(team, member2, false));

        //when
        final TeamDto response = teamService.findById(team.getId());

        //then
        assertThat(response.getTitle()).isEqualTo(team.getTitle());
        assertThat(response.getHostId()).isEqualTo(member1.getId());
        assertThat(response.getParticipants()).hasSize(2);
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("id에 해당하는 팀 정보를 변경한다.")
        void success() {
            // given
            final Member member1 = getMember("릭");
            final Member member2 = getMember("페퍼");
            final Team team = getTeam("잠실 제이슨조");

            participantRepository.save(new Participant(team, member1, true));
            participantRepository.save(new Participant(team, member2, false));

            final TeamUpdateDto request = new TeamUpdateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3));

            // when
            teamService.update(request, team.getId(), member1.getId());

            // then
            final Team actualTeam = teamRepository.findById(team.getId()).orElseThrow();
            assertThat(actualTeam.getTitle()).isEqualTo(request.getTitle());
            assertThat(actualTeam.getPlace()).isEqualTo(request.getPlace());
            assertThat(actualTeam.getStartAt()).isEqualTo(request.getStartAt());
        }

        @Test
        @DisplayName("호스트가 아닌 멤버가 팀을 수정하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = getMember("릭");
            final Member member2 = getMember("페퍼");
            final Team team = getTeam("잠실 제이슨조");

            participantRepository.save(new Participant(team, member1, true));
            participantRepository.save(new Participant(team, member2, false));

            final TeamUpdateDto request = new TeamUpdateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3));

            // when, then
            final Long memberId = member2.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.update(request, teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("delete 메서드는 id에 해당하는 팀을 삭제한다.")
        void success() {
            // given
            final Member member1 = getMember("릭");
            final Member member2 = getMember("페퍼");
            final Team team = getTeam("잠실 제이슨조");

            participantRepository.save(new Participant(team, member1, true));
            participantRepository.save(new Participant(team, member2, false));

            // when
            teamService.deleteById(team.getId(), member1.getId());

            // then
            assertTrue(teamRepository.findById(team.getId()).isEmpty());
        }

        @Test
        @DisplayName("delete 메서드는 호스트가 아닌 멤버가 팀을 삭제하는 경우 예외를 던진다.")
        void hostUnauthorized_Exception() {
            // given
            final Member member1 = getMember("릭");
            final Member member2 = getMember("페퍼");
            final Team team = getTeam("잠실 제이슨조");

            participantRepository.save(new Participant(team, member1, true));
            participantRepository.save(new Participant(team, member2, false));

            // when, then
            final Long memberId = member2.getId();
            final Long teamId = team.getId();
            assertThatThrownBy(() -> teamService.deleteById(teamId, memberId))
                    .isInstanceOf(HostUnauthorizedException.class)
                    .hasMessageContaining("호스트 권한이 없습니다.");
        }
    }

    private Member getMember(final String nickname) {
        return memberRepository.save(new Member(nickname, (int) System.nanoTime(), "profile.png"));
    }

    private Team getTeam(final String title) {
        return teamRepository.save(new Team(title, "피니시방", LocalDateTime.now().plusDays(3), "jason.png"));
    }
}
