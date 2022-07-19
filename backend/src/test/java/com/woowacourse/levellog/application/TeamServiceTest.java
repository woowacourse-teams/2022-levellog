package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.ParticipantIdsRequest;
import com.woowacourse.levellog.dto.TeamRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save 메서드는 팀을 생성한다.")
    void save() {
        //given
        final Long participant1 = memberRepository.save(new Member("알린", 1111, "alien.img")).getId();
        final Long participant2 = memberRepository.save(new Member("페퍼", 2222, "pepper.img")).getId();
        final Long participant3 = memberRepository.save(new Member("로마", 3333, "roma.img")).getId();
        final TeamRequest teamRequest = new TeamRequest("잠실 준조", "트랙룸", LocalDateTime.now(),
                new ParticipantIdsRequest(List.of(participant1, participant2, participant3)));

        //when
        final Long id = teamService.save(participant1, teamRequest);

        //then
        final Optional<Team> team = teamRepository.findById(id);
        assertThat(team).isPresent();
    }
}
