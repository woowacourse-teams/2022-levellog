package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
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

    private LocalDateTime setTeamStartAt() {
        return LocalDateTime.now().plusDays(1);
    }

    @Nested
    @DisplayName("save 메서드는")
    class SaveTest {

        @Test
        @DisplayName("레벨로그를 저장한다.")
        void success() {
            // given
            final LevellogWriteDto request = new LevellogWriteDto("Spring을 학습하였습니다.");
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));

            // when
            final Long id = levellogService.save(request, member.getId(), team.getId());

            // then
            final Optional<Levellog> levellog = levellogRepository.findById(id);
            assertThat(levellog).isPresent();
        }

        @Test
        @DisplayName("레벨로그의 팀이 존재하지 않는 경우 예외를 던진다.")
        void save_teamNotFound_exception() {
            // given
            final LevellogWriteDto request = new LevellogWriteDto("스프링에 대해 학습하였습니다.");
            final Long memberId = memberRepository.save(new Member("알린", 1111, "alien.img")).getId();
            final Long teamId = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl")).getId();
            teamRepository.deleteById(teamId);

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, memberId, teamId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessage("팀이 존재하지 않습니다. id : " + teamId);
        }

        @Test
        @DisplayName("레벨로그의 작성자가 존재하지 않는 경우 예외를 던진다.")
        void save_memberNotFound_exception() {
            // given
            final LevellogWriteDto request = new LevellogWriteDto("스프링에 대해 학습하였습니다.");
            final Long teamId = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl")).getId();
            final Long memberId = memberRepository.save(new Member("알린", 1111, "alien.img")).getId();
            memberRepository.deleteById(memberId);

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, memberId, teamId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage("멤버가 존재하지 않습니다. id : " + memberId);
        }

        @Test
        @DisplayName("팀에서 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExist_exception() {
            // given
            final LevellogWriteDto request = new LevellogWriteDto("굳굳");
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long memberId = member.getId();
            final Long teamId = team.getId();

            levellogRepository.save(Levellog.of(member, team, "굳굳굳"));

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, memberId, teamId))
                    .isInstanceOf(LevellogAlreadyExistException.class)
                    .hasMessage("레벨로그를 이미 작성하였습니다. authorId : " + memberId + " teamId : " + teamId);
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
        void save_contentBlank_exception(final String invalidContent) {
            // given
            final LevellogWriteDto request = new LevellogWriteDto(invalidContent);
            final Long memberId = memberRepository.save(new Member("알린", 1111, "alien.img")).getId();
            final Long teamId = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl")).getId();

            //  when & then
            assertThatThrownBy(() -> levellogService.save(request, memberId, teamId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessage("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindByIdTest {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 조회한다.")
        void success() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final String content = "content";
            final Levellog levellog = levellogRepository.save(Levellog.of(member, team, content));

            // when
            final LevellogDto response = levellogService.findById(levellog.getId());

            // then
            assertThat(response.getContent()).isEqualTo(content);
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void findById_notFound_exception() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();
            levellogRepository.deleteById(levellogId);

            // when & then
            assertThatThrownBy(() -> levellogService.findById(levellogId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessage("레벨로그가 존재하지 않습니다. id : " + levellogId);
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class UpdateTest {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 변경한다.")
        void success() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(Levellog.of(member, team, "original content"));
            final LevellogWriteDto request = new LevellogWriteDto("update content");

            // when
            levellogService.update(request, levellog.getId(), member.getId());

            // then
            final Levellog actual = levellogRepository.findById(levellog.getId()).orElseThrow();
            assertThat(actual.getContent()).isEqualTo(request.getContent());
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void update_notFound_exception() {
            // given
            final LevellogWriteDto request = new LevellogWriteDto("update content");
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();
            final Long memberId = member.getId();
            levellogRepository.deleteById(levellogId);

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessage("레벨로그가 존재하지 않습니다. id : " + levellogId);
        }

        @Test
        @DisplayName("memberId에 해당하는 작성자가 존재하지 않는 경우 예외를 던진다.")
        void update_memberNotFound_exception() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "Spring을 학습하였습니다.")).getId();
            final LevellogWriteDto request = new LevellogWriteDto("JPA를 학습하였습니다.");
            final Long memberId = member.getId();
            memberRepository.deleteById(memberId);

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, memberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage("멤버가 존재하지 않습니다. id : " + memberId);
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_Exception() {
            // given
            final Long memberId = memberRepository.save(new Member("페퍼", 1111, "pepper.img")).getId();
            final Member member = memberRepository.save(new Member("알린", 2222, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();
            final LevellogWriteDto request = new LevellogWriteDto("update content");

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, memberId))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("수정한 레벨로그의 내용이 공백이나 null일 경우 예외를 던진다.")
        void update_contentBlank_Exception(final String invalidContent) {
            // given
            final LevellogWriteDto request = new LevellogWriteDto(invalidContent);
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();
            final Long memberId = member.getId();

            //  when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, memberId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessage("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteByIdTest {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 삭제한다.")
        void success() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Levellog levellog = levellogRepository.save(Levellog.of(member, team, "original content"));

            // when
            levellogService.deleteById(levellog.getId(), member.getId());

            // then
            final Optional<Levellog> actual = levellogRepository.findById(levellog.getId());
            assertThat(actual).isEmpty();
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void deleteById_notFound_exception() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();
            final Long memberId = member.getId();
            levellogRepository.deleteById(levellogId);

            // when & then
            assertThatThrownBy(() -> levellogService.deleteById(levellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessage("레벨로그가 존재하지 않습니다. id : " + levellogId);
        }

        @Test
        @DisplayName("memberId에 해당하는 작성자가 존재하지 않는 경우 예외를 던진다.")
        void deleteById_memberNotFound_exception() {
            // given
            final Member member = memberRepository.save(new Member("알린", 1111, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "Spring을 학습하였습니다.")).getId();
            final Long memberId = member.getId();
            memberRepository.deleteById(memberId);

            // when & then
            assertThatThrownBy(() -> levellogService.deleteById(levellogId, memberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage("멤버가 존재하지 않습니다. id : " + memberId);
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_Exception() {
            // given
            final Long memberId = memberRepository.save(new Member("페퍼", 1111, "pepper.img")).getId();
            final Member member = memberRepository.save(new Member("알린", 2222, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "잠실 트랙룸", setTeamStartAt(), "profileUrl"));
            final Long levellogId = levellogRepository.save(Levellog.of(member, team, "original content")).getId();

            // when & then
            assertThatThrownBy(() -> levellogService.deleteById(levellogId, memberId))
                    .isInstanceOf(UnauthorizedException.class);
        }
    }
}
