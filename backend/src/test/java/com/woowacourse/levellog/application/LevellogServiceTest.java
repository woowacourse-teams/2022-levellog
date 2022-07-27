package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogCreateDto;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
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
        final LevellogDto response = levellogService.findById(levellog.getId());

        // then
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("레벨로그를 저장한다.")
        void save() {
            // given
            final LevellogCreateDto request = new LevellogCreateDto("굳굳");
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));

            // when
            final Long id = levellogService.save(request, author.getId(), team.getId());

            // then
            final Optional<Levellog> levellog = levellogRepository.findById(id);
            assertThat(levellog).isPresent();
        }

        @Test
        @DisplayName("팀에 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() {
            // given
            final LevellogCreateDto request = new LevellogCreateDto("굳굳");
            final Member author = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Long authorId = author.getId();
            final Long teamId = team.getId();

            levellogRepository.save(new Levellog(author, team, "굳굳굳"));

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, authorId, teamId))
                    .isInstanceOf(LevellogAlreadyExistException.class);
        }
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
            final LevellogCreateDto request = new LevellogCreateDto("update content");

            // when
            levellogService.update(request, levellog.getId(), author.getId());

            // then
            final Levellog actual = levellogRepository.findById(levellog.getId()).orElseThrow();
            assertThat(actual.getContent()).isEqualTo(request.getContent());
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void update_Unauthorized_Exception() {
            // given
            final Long memberId = memberRepository.save(new Member("페퍼", 1111, "pepper.img")).getId();
            final Member author = memberRepository.save(new Member("알린", 2222, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Long levellogId = levellogRepository.save(new Levellog(author, team, "original content")).getId();
            final LevellogCreateDto request = new LevellogCreateDto("update content");

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, memberId))
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
            final Long memberId = memberRepository.save(new Member("페퍼", 1111, "pepper.img")).getId();
            final Member author = memberRepository.save(new Member("알린", 2222, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", LocalDateTime.now(), "profileUrl"));
            final Long levellogId = levellogRepository.save(new Levellog(author, team, "original content")).getId();

            // when & then
            assertThatThrownBy(() -> levellogService.deleteById(levellogId, memberId))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }
}
