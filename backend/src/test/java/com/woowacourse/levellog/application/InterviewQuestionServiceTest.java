package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionWriteDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("InterviewQuestionService 클래스의")
class InterviewQuestionServiceTest extends ServiceTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("인터뷰 질문을 저장한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            timeStandard.setInProgress();

            // when
            final Long id = interviewQuestionService.save(request, pepperLevellogId, eve.getId());

            // then
            assertThat(interviewQuestionRepository.findById(id))
                    .isPresent();
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("인터뷰 내용이 공백이나 null일 경우 예외를 던진다.")
        void save_contentBlank_exception(final String invalidContent) {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from(invalidContent);
            final Long authorId = eve.getId();

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, authorId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessage("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void save_memberNotFound_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");

            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않습니다.", String.valueOf(invalidMemberId));
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void save_levellogNotFound_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            saveTeam(pepper, eve);
            final Long memberId = pepper.getId();
            final Long invalidLevellogId = 1000L;
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 인터뷰 질문을 작성할 경우 예외를 던진다.")
        void save_otherTeamMember_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Long otherTeamMemberId = saveMember("알린").getId();
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, otherTeamMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("같은 팀에 속한 멤버만 인터뷰 질문을 작성할 수 있습니다.", String.valueOf(team.getId()),
                            String.valueOf(pepperLevellogId), String.valueOf(otherTeamMemberId));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void save_isClosed_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, eve.getId()))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void save_isReady_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전에 인터뷰 질문을 작성 할 수 없습니다.", String.valueOf(team.getId()));
        }

        @Test
        @DisplayName("자신에게 인터뷰 질문을 작성하는 경우 예외를 던진다.")
        void save_selfInterviewQuestion_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");

            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, pepper.getId()))
                    .isInstanceOf(InvalidInterviewQuestionException.class)
                    .hasMessageContainingAll("잘못된 인터뷰 질문 요청입니다.", String.valueOf(pepperLevellogId),
                            String.valueOf(pepper.getId()));
        }
    }

    @Nested
    @DisplayName("findAllByLevellog 메서드는")
    class FindAllByLevellog {

        private List<String> toContents(final InterviewQuestionDto response) {
            return response.getContents()
                    .stream()
                    .map(InterviewQuestionContentDto::getContent)
                    .collect(Collectors.toList());
        }

        @Test
        @DisplayName("레벨로그에 작성된 모든 인터뷰 질문을 조회한다.")
        void success() {
            // given
            final Member harry = saveMember("해리");
            final Member roma = saveMember("로마");
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Team team = saveTeam(harry, roma, pepper, rick);

            final Levellog levellog = saveLevellog(harry, team);
            final Long levellogId = levellog.getId();

            saveInterviewQuestion("로마 - 1", levellog, roma);

            saveInterviewQuestion("릭 - 1", levellog, rick);

            saveInterviewQuestion("페퍼 - 1", levellog, pepper);
            saveInterviewQuestion("페퍼 - 2", levellog, pepper);
            saveInterviewQuestion("페퍼 - 3", levellog, pepper);

            saveInterviewQuestion("로마 - 2", levellog, roma);

            // when
            final List<InterviewQuestionDto> actual = interviewQuestionService.findAllByLevellog(levellogId)
                    .getInterviewQuestions();

            // then
            final InterviewQuestionDto romaResponse = actual.get(0);
            final InterviewQuestionDto rickResponse = actual.get(1);
            final InterviewQuestionDto pepperResponse = actual.get(2);

            assertAll(
                    () -> assertThat(actual).hasSize(3),

                    () -> assertThat(romaResponse.getAuthor().getNickname()).isEqualTo("로마"),
                    () -> assertThat(toContents(romaResponse)).containsExactly("로마 - 1", "로마 - 2"),

                    () -> assertThat(rickResponse.getAuthor().getNickname()).isEqualTo("릭"),
                    () -> assertThat(toContents(rickResponse)).containsExactly("릭 - 1"),

                    () -> assertThat(pepperResponse.getAuthor().getNickname()).isEqualTo("페퍼"),
                    () -> assertThat(toContents(pepperResponse)).containsExactly("페퍼 - 1", "페퍼 - 2", "페퍼 - 3")
            );
        }

        @Test
        @DisplayName("id에 해당하는 레벨로그가 존재하지 않으면 예외를 던진다.")
        void findAllByLevellog_levellogNotExist_exception() {
            // given
            final Long invalidLevellogId = 999L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.findAllByLevellog(invalidLevellogId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(invalidLevellogId));
        }
    }

    @Nested
    @DisplayName("findAllByLevellogAndAuthor 메서드는")
    class FindAllByLevellogAndAuthor {

        @Test
        @DisplayName("레벨로그에 대해 특정 멤버가 작성한 인터뷰 질문 목록을 조회한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            saveInterviewQuestion("스프링이란?", pepperLevellog, eve);
            saveInterviewQuestion("스프링 빈이란?", pepperLevellog, eve);

            // when
            final InterviewQuestionContentsDto response = interviewQuestionService.findAllByLevellogAndAuthor(
                    pepperLevellog.getId(),
                    eve.getId());

            // then
            final List<String> actualInterviewQuestionContents = response.getInterviewQuestions()
                    .stream()
                    .map(InterviewQuestionContentDto::getContent)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(response.getInterviewQuestions()).hasSize(2),
                    () -> assertThat(actualInterviewQuestionContents).contains("스프링이란?", "스프링 빈이란?")
            );
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void findAllByLevellogAndAuthor_levellogNotFound_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            saveTeam(pepper, eve);
            final Long memberId = eve.getId();
            final Long invalidLevellogId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.findAllByLevellogAndAuthor(invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void findAllByLevellogAndAuthor_memberNotFound_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Team team = saveTeam(pepper);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.findAllByLevellogAndAuthor(pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않습니다.", String.valueOf(invalidMemberId));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("인터뷰 질문을 수정한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("업데이트된 질문 내용");

            timeStandard.setInProgress();

            // when
            interviewQuestionService.update(request, interviewQuestionId, eve.getId());

            // then
            final List<String> actualInterviewQuestions = interviewQuestionRepository
                    .findAllByLevellogAndAuthor(pepperLevellog, eve)
                    .stream()
                    .map(InterviewQuestion::getContent)
                    .collect(Collectors.toList());
            assertThat(actualInterviewQuestions).contains("업데이트된 질문 내용");
        }

        @Test
        @DisplayName("인터뷰 질문이 존재하지 않는 경우 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() {
            // given
            final Member eve = saveMember("이브");
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("업데이트된 질문 내용");
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("인터뷰 질문이 존재하지 않습니다.", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Long otherMemberId = saveMember("릭").getId();
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("업데이트된 질문 내용");

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 수정할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void update_isClosed_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("업데이트된 질문 내용");

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, eve.getId()))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void update_isReady_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionWriteDto request = InterviewQuestionWriteDto.from("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전에 인터뷰 질문을 수정 할 수 없습니다.", String.valueOf(team.getId()));
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("인터뷰 질문을 삭제한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            timeStandard.setInProgress();

            // when
            interviewQuestionService.deleteById(interviewQuestionId, eve.getId());

            // then
            assertThat(interviewQuestionRepository.findById(interviewQuestionId))
                    .isEmpty();
        }

        @Test
        @DisplayName("인터뷰 질문이 존재하지 않는 경우 예외를 던진다.")
        void deleteById_interviewQuestionNotFound_exception() {
            // given
            final Member eve = saveMember("이브");
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("인터뷰 질문이 존재하지 않습니다", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Long otherMemberId = saveMember("릭").getId();
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 삭제할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void deleteById_isClosed_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, eve.getId()))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }

        @Test
        @DisplayName("인터뷰가 시작 전이면 예외를 던진다.")
        void deleteById_isReady_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, eve.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContainingAll("인터뷰 시작 전에 인터뷰 질문을 삭제 할 수 없습니다.", String.valueOf(team.getId()));
        }
    }
}
