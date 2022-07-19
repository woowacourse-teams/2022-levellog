package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Participant;
import com.woowacourse.levellog.domain.ParticipantRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.ParticipantIdsRequest;
import com.woowacourse.levellog.dto.ParticipantsResponse;
import com.woowacourse.levellog.dto.TeamRequest;
import com.woowacourse.levellog.dto.TeamResponse;
import com.woowacourse.levellog.dto.TeamsResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("LevellogService의")
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
        final TeamRequest teamRequest = new TeamRequest("잠실 준조", "트랙룸", LocalDateTime.now(),
                new ParticipantIdsRequest(List.of(participant1, participant2, participant3)));

        //when
        final Long id = teamService.save(participant1, teamRequest);

        //then
        final Optional<Team> team = teamRepository.findById(id);
        assertThat(team).isPresent();
    }

    @Test
    @DisplayName("findAll 메서드는 전체 팀 목록을 조회한다.")
    void findAll() {
        //given
        Member member1 = getMember("릭");
        Member member2 = getMember("페퍼");

        Team team1 = getTeam("잠실 제이슨조");
        Team team2 = getTeam("선릉 브라운조");

        participantRepository.save(new Participant(team1, member1, true));
        participantRepository.save(new Participant(team1, member2, false));
        participantRepository.save(new Participant(team2, member1, false));
        participantRepository.save(new Participant(team2, member2, true));

        //when
        final TeamsResponse response = teamService.findAll();

        List<String> actualTitles = response.getTeams()
                .stream()
                .map(TeamResponse::getTitle)
                .collect(Collectors.toList());

        List<Long> actualHostIds = response.getTeams()
                .stream()
                .map(TeamResponse::getHostId)
                .collect(Collectors.toList());

        List<Integer> actualParticipantSizes = response.getTeams()
                .stream()
                .map(TeamResponse::getParticipants)
                .map(ParticipantsResponse::getParticipants)
                .map(List::size)
                .collect(Collectors.toList());
        //then
        assertThat(actualTitles).containsExactly(team1.getTitle(), team2.getTitle());
        assertThat(actualHostIds).containsExactly(member1.getId(), member2.getId());
        assertThat(actualParticipantSizes).containsExactly(2, 2);

        assertThat(response.getTeams()).hasSize(2);
    }

    private Member getMember(String nickname) {
        return memberRepository.save(new Member(nickname, (int) System.currentTimeMillis(), "profile.png"));
    }

    private Team getTeam(final String title) {
        return teamRepository.save(new Team(title, "피니시방", LocalDateTime.now(), "jason.png"));
    }
}
