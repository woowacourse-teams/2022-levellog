package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.levellog.dto.LevellogResponse;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.UnauthorizedException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.Optional;
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

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 변경한다.")
        void success() {
            // given
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));
            final LevellogRequest request = new LevellogRequest("update content");

            // when
            levellogService.update(levellog.getId(), author.getId(), request);

            // then
            final Levellog actual = levellogRepository.findById(levellog.getId()).orElseThrow();
            assertThat(actual.getContent()).isEqualTo(request.getContent());
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void unAuthorize_Exception() {
            // given
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));
            final LevellogRequest request = new LevellogRequest("update content");

            // when & then
            assertThatThrownBy(() -> levellogService.update(levellog.getId(), 100L, request))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class deleteById {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 삭제한다.")
        void success() {
            // given
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));

            // when
            levellogService.deleteById(levellog.getId(), author.getId());

            // then
            final Optional<Levellog> actual = levellogRepository.findById(levellog.getId());
            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void unAuthorize_Exception() {
            // given
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(new Levellog(author, team, "original content"));

            // when & then
            assertThatThrownBy(() -> levellogService.deleteById(levellog.getId(), 12345L))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("레벨로그를 저장한다.")
        void save() {
            // given
            final LevellogRequest request = new LevellogRequest("굳굳");
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));

            // when
            final Long id = levellogService.save(author.getId(), team.getId(), request);

            // then
            final Optional<Levellog> levellog = levellogRepository.findById(id);
            assertThat(levellog).isPresent();
        }

        @Test
        @DisplayName("팀에 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() {
            // given
            final LevellogRequest request = new LevellogRequest("굳굳");
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));

            levellogRepository.save(new Levellog(author, team, "굳굳굳"));

            // when, then
            assertThatThrownBy(() -> levellogService.save(author.getId(), team.getId(), request))
                    .isInstanceOf(LevellogAlreadyExistException.class);
        }
    }
}
