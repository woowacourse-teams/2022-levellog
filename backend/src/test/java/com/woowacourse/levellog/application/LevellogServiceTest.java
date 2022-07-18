package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Transactional
@DisplayName("LevellogService의")
class LevellogServiceTest {
    @Autowired
    private LevellogService levellogService;
    @Autowired
    private LevellogRepository levellogRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    @DisplayName("save 메서드는 레벨로그를 저장한다.")
    void save() {
        // given
        final LevellogRequest request = new LevellogRequest("굳굳");
        final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));

        // when
        final Long id = levellogService.save(author, team.getId(), request);

        // then
        final Optional<Levellog> levellog = levellogRepository.findById(id);
        assertThat(levellog).isPresent();
    }

    @Test
    @DisplayName("findById 메서드는 id에 해당하는 레벨로그를 조회한다.")
    void findById() {
        // given
        final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
        final String content = "content";
        final Levellog levellog = levellogRepository.save(new Levellog(author, team, content));

        // when
        final LevellogResponse response = levellogService.findById(levellog.getId());

        // then
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("update 메서드는 id에 해당하는 레벨로그를 변경한다.")
    void update() {
        // given
        final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
        final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));
        final LevellogRequest request = new LevellogRequest("update content");

        // when
        levellogService.update(levellog.getId(), request);

        // then
        final Levellog actual = levellogRepository.findById(levellog.getId()).orElseThrow();
        assertThat(actual.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("deleteById 메서드는 id에 해당하는 레벨로그를 삭제한다.")
    void deleteById() {
        // given
        final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
        final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));

        // when
        levellogService.deleteById(levellog.getId());

        // then
        final Optional<Levellog> actual = levellogRepository.findById(levellog.getId());
        assertThat(actual).isEmpty();
    }
}
