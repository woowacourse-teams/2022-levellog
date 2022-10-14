package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.levellog.dto.response.LevellogDetailResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponses;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.exception.TeamNotReadyException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("LevellogService의")
class LevellogServiceTest extends ServiceTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("레벨로그를 저장한다.")
        void success() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("Spring을 학습하였습니다.");
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Long authorId = author.getId();
            final Long teamId = saveTeam(author, pepper)
                    .getId();

            // when
            final Long savedLevellogId = levellogService.save(request, getLoginStatus(author), teamId);

            // then
            final Optional<Levellog> levellog = levellogRepository.findById(savedLevellogId);
            assertThat(levellog).isPresent();
        }

        @Test
        @DisplayName("레벨로그의 팀이 존재하지 않는 경우 예외를 던진다.")
        void save_teamNotFound_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("스프링에 대해 학습하였습니다.");
            final Member author = saveMember("알린");
            final Long teamId = 1000L;

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, getLoginStatus(author), teamId))
                    .isInstanceOf(TeamNotFoundException.class)
                    .hasMessageContainingAll("팀이 존재하지 않습니다.", String.valueOf(teamId));
        }

        @Test
        @DisplayName("팀에서 이미 레벨로그를 작성한 경우 새로운 레벨로그를 작성하면 예외를 던진다.")
        void save_alreadyExist_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("굳굳");
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Long authorId = author.getId();
            final Long teamId = saveTeam(author, pepper)
                    .getId();

            levellogService.save(request, getLoginStatus(author), teamId);

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, getLoginStatus(author), teamId))
                    .isInstanceOf(LevellogAlreadyExistException.class)
                    .hasMessageContainingAll("레벨로그가 이미 존재합니다.", String.valueOf(author.getId()), String.valueOf(teamId));
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("레벨로그 내용이 공백이나 null일 경우 예외를 던진다.")
        void save_contentBlank_exception(final String invalidContent) {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest(invalidContent);
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Long authorId = author.getId();
            final Long teamId = saveTeam(author, pepper)
                    .getId();

            //  when & then
            assertThatThrownBy(() -> levellogService.save(request, getLoginStatus(author), teamId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 요청한 경우 예외를 던진다.")
        void save_notReady_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("Spring을 학습하였습니다.");
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Long teamId = saveTeam(author, pepper).getId();

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, getLoginStatus(author), teamId))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.");
        }

        @Test
        @DisplayName("작성자가 팀에 속한 멤버가 아닌 경우 예외를 던진다.")
        void save_notTeamMember_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("Spring을 학습하였습니다.");
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Long teamId = saveTeam(pepper, rick).getId();

            // when & then
            assertThatThrownBy(() -> levellogService.save(request, getLoginStatus(author), teamId))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContaining("팀 참가자가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 조회한다.")
        void success() {
            // given
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(author, pepper);
            final String content = "content";
            final Levellog levellog = saveLevellog(author, team, content);

            // when
            final LevellogDetailResponse response = levellogService.findById(levellog.getId());

            // then
            assertAll(
                    () -> assertThat(response.getAuthor().getId()).isEqualTo(author.getId()),
                    () -> assertThat(response.getContent()).isEqualTo(content)
            );
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void findById_notFound_exception() {
            // when & then
            assertThatThrownBy(() -> levellogService.findById(1000L))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(1000L));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("id에 해당하는 레벨로그를 변경한다.")
        void success() {
            // given
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(author, pepper);

            final Levellog levellog = saveLevellog(author, team);
            final LevellogWriteRequest request = new LevellogWriteRequest("update content");

            // when
            levellogService.update(request, levellog.getId(), getLoginStatus(author));

            // then
            final Levellog actual = levellogRepository.findById(levellog.getId()).orElseThrow();
            assertThat(actual.getContent()).isEqualTo(request.getContent());
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void update_notFound_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("update content");
            final Member author = saveMember("알린");
            final Long levellogId = 1000L;

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, getLoginStatus(author)))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(levellogId));
        }

        @Test
        @DisplayName("작성자의 id와 로그인한 id가 다를 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() {
            // given
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(author, pepper);
            final Long levellogId = saveLevellog(author, team).getId();

            final LevellogWriteRequest request = new LevellogWriteRequest("update content");
            final Member otherMember = saveMember("페퍼");

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, getLoginStatus(otherMember)))
                    .isInstanceOf(MemberNotAuthorException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("수정한 레벨로그의 내용이 공백이나 null일 경우 예외를 던진다.")
        void update_contentBlank_exception(final String invalidContent) {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest(invalidContent);
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(author, pepper);
            final Long levellogId = saveLevellog(author, team).getId();

            //  when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, getLoginStatus(author)))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("레벨로그 내용은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("Ready 상태가 아닐 때 요청한 경우 예외를 반환한다.")
        void update_notReady_exception() {
            // given
            final LevellogWriteRequest request = new LevellogWriteRequest("update content");
            final Member author = saveMember("알린");
            final Member pepper = saveMember("페퍼");

            final Team team = saveTeam(author, pepper);
            final Long levellogId = saveLevellog(author, team).getId();

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(() -> levellogService.update(request, levellogId, getLoginStatus(author)))
                    .isInstanceOf(TeamNotReadyException.class)
                    .hasMessageContaining("인터뷰 준비 상태가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("findAllByAuthorId 메서드는")
    class FindAllByAuthorId {

        @Test
        @DisplayName("주어진 authorId에 해당하는 레벨로그를 모두 조회한다.")
        void success() {
            // given
            final Member author = saveMember("페퍼");
            final Member alien = saveMember("알린");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(author, alien);
            final Team team2 = saveTeam(author, roma);

            saveLevellog(author, team);
            saveLevellog(author, team2);

            // when
            final LevellogResponses levellogResponses = levellogService.findAllByAuthorId(getLoginStatus(author));

            // then
            assertThat(levellogResponses.getLevellogs()).hasSize(2);
        }
    }
}
