package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDetailDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionsDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
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
    class SaveTest {

        @Test
        @DisplayName("인터뷰 질문을 저장한다.")
        void success() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when
            final Long id = interviewQuestionService.save(request, pepperLevellog.getId(), eve.getId());

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
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Long pepperLevellogId = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용")).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from(invalidContent);
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, authorId))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessage("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void save_memberNotFound_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img"));
            participantRepository.save(new Participant(team, pepper, true));
            final Long pepperLevellogId = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용")).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 멤버", String.valueOf(invalidMemberId));
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void save_levellogNotFound_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            saveTeamAndTwoParticipants(pepper, eve);
            final Long memberId = pepper.getId();
            final Long invalidLevellogId = 1000L;
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 레벨로그", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 인터뷰 질문을 작성할 경우 예외를 던진다.")
        void save_otherTeamMember_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Long otherTeamMemberId = memberRepository.save(new Member("알린", 3333, "alien.img")).getId();
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Long pepperLevellogId = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용")).getId();
            final InterviewQuestionDto request = InterviewQuestionDto.from("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, otherTeamMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("같은 팀에 속한 멤버만 인터뷰 질문을 작성할 수 있습니다.", String.valueOf(team.getId()),
                            String.valueOf(pepperLevellogId), String.valueOf(otherTeamMemberId));
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAllTest {

        @Test
        @DisplayName("레벨로그에 대해 특정 멤버가 작성한 인터뷰 질문 목록을 조회한다.")
        void success() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            saveInterviewQuestion("스프링이란?", pepperLevellog, eve);
            saveInterviewQuestion("스프링 빈이란?", pepperLevellog, eve);

            // when
            final InterviewQuestionsDto response = interviewQuestionService.findAll(pepperLevellog.getId(),
                    eve.getId());

            // then
            final List<String> actualInterviewQuestionContents = response.getInterviewQuestions()
                    .stream()
                    .map(InterviewQuestionDetailDto::getInterviewQuestion)
                    .collect(Collectors.toList());

            assertAll(
                    () -> assertThat(response.getInterviewQuestions()).hasSize(2),
                    () -> assertThat(actualInterviewQuestionContents).contains("스프링이란?", "스프링 빈이란?")
            );
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void findAll_levellogNotFound_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            saveTeamAndTwoParticipants(pepper, eve);
            final Long memberId = eve.getId();
            final Long invalidLevellogId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.findAll(invalidLevellogId, memberId))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 레벨로그", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 존재하지 않는 경우 예외를 던진다.")
        void findAll_memberNotFound_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img"));
            participantRepository.save(new Participant(team, pepper, true));
            final Long pepperLevellogId = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용")).getId();
            final Long invalidMemberId = 1000L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.findAll(pepperLevellogId, invalidMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 멤버", String.valueOf(invalidMemberId));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class UpdateTest {

        @Test
        @DisplayName("인터뷰 질문을 수정한다.")
        void success() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

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
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 인터뷰 질문", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 2222, "image.png"));
            final Long otherMemberId = memberRepository.save(new Member("릭", 123123, "image.png")).getId();
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);
            final InterviewQuestionDto request = InterviewQuestionDto.from("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 수정할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteByIdTest {

        @Test
        @DisplayName("인터뷰 질문을 삭제한다.")
        void success() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);

            // when
            interviewQuestionService.deleteById(interviewQuestionId, eve.getId());

            // then
            assertThat(interviewQuestionRepository.findById(interviewQuestionId))
                    .isEmpty();
        }

        @Test
        @DisplayName("인터뷰 질문이 존재하지 않는 경우 예외를 던진다.")
        void update_interviewQuestionNotFound_exception() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 123123, "image.png"));
            final Long invalidInterviewQuestionId = 1000L;
            final Long authorId = eve.getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(invalidInterviewQuestionId, authorId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("존재하지 않는 인터뷰 질문", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() {
            // given
            final Member pepper = memberRepository.save(new Member("페퍼", 1111, "pepper.png"));
            final Member eve = memberRepository.save(new Member("이브", 2222, "image.png"));
            final Long otherMemberId = memberRepository.save(new Member("릭", 123123, "image.png")).getId();
            final Team team = saveTeamAndTwoParticipants(pepper, eve);
            final Levellog pepperLevellog = levellogRepository.save(Levellog.of(pepper, team, "레벨로그 작성 내용"));
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, otherMemberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 삭제할 수 있는 권한이 없습니다.", String.valueOf(otherMemberId),
                            String.valueOf(eve.getId()), String.valueOf(pepperLevellog.getId()));
        }
    }

    private Team saveTeamAndTwoParticipants(final Member participant1, final Member participant2) {
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img"));
        participantRepository.save(new Participant(team, participant1, true));
        participantRepository.save(new Participant(team, participant2, false));
        return team;
    }

    private Long saveInterviewQuestion(final String content, final Levellog levellog, final Member author) {
        final InterviewQuestionDto request = InterviewQuestionDto.from(content);
        return interviewQuestionService.save(request, levellog.getId(), author.getId());
    }
}
