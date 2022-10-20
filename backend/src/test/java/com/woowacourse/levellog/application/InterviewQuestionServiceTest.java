package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResults;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponse;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponses;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionResponse;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikeNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikesAlreadyExistException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            timeStandard.setInProgress();

            // when
            final Long id = interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(eve));

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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest(invalidContent);

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(eve)))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("인터뷰 질문은 공백이나 null일 수 없습니다.");
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않는 경우 예외를 던진다.")
        void save_levellogNotFound_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            saveTeam(pepper, eve);
            final Long invalidLevellogId = 1000L;
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, invalidLevellogId, getLoginStatus(eve)))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(invalidLevellogId));
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 인터뷰 질문을 작성할 경우 예외를 던진다.")
        void save_otherTeamMember_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(alien)))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContainingAll("팀 참가자가 아닙니다.",
                            String.valueOf(team.getId()), String.valueOf(alien.getId()));
        }

        @Test
        @DisplayName("인터뷰가 종료된 후면 예외를 던진다.")
        void save_isClosed_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(eve)))
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(eve)))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContainingAll("인터뷰 진행중인 상태가 아닙니다.");
        }

        @Test
        @DisplayName("자신에게 인터뷰 질문을 작성하는 경우 예외를 던진다.")
        void save_selfInterviewQuestion_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");

            final Team team = saveTeam(pepper, eve);
            final Long pepperLevellogId = saveLevellog(pepper, team).getId();
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.save(request, pepperLevellogId, getLoginStatus(pepper)))
                    .isInstanceOf(InvalidInterviewQuestionException.class)
                    .hasMessageContainingAll("잘못된 인터뷰 질문 요청입니다.", String.valueOf(pepperLevellogId),
                            String.valueOf(pepper.getId()));
        }
    }

    @Nested
    @DisplayName("findAllByLevellog 메서드는")
    class FindAllByLevellog {

        private List<String> toContents(final InterviewQuestionResponse response) {
            return response.getContents()
                    .stream()
                    .map(InterviewQuestionContentResponse::getContent)
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
            final List<InterviewQuestionResponse> actual = interviewQuestionService.findAllByLevellog(levellogId)
                    .getInterviewQuestions();

            // then
            final InterviewQuestionResponse romaResponse = actual.get(0);
            final InterviewQuestionResponse rickResponse = actual.get(1);
            final InterviewQuestionResponse pepperResponse = actual.get(2);

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
            final InterviewQuestionContentResponses response = interviewQuestionService.findAllByLevellogAndAuthor(
                    pepperLevellog.getId(), getLoginStatus(eve));

            // then
            final List<String> actualInterviewQuestionContents = response.getInterviewQuestions()
                    .stream()
                    .map(InterviewQuestionContentResponse::getContent)
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
            final Long invalidLevellogId = 1000L;

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.findAllByLevellogAndAuthor(invalidLevellogId, getLoginStatus(eve)))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContainingAll("레벨로그가 존재하지 않습니다.", String.valueOf(invalidLevellogId));
        }
    }

    @Nested
    @DisplayName("searchByKeyword 메서드는")
    class SearchByKeyword {

        @Test
        @DisplayName("인터뷰 질문을 최신순으로 정렬하여 조회한다.")
        void searchByKeyword_latestSort_success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            final Long size = 10L;
            final Long page = 0L;
            final String sort = "latest";

            timeStandard.setInProgress();

            final Long question1Id = saveInterviewQuestion("스프링 A", pepperLevellog, eve).getId();
            final Long question2Id = saveInterviewQuestion("스프링 B", pepperLevellog, eve).getId();
            final Long question3Id = saveInterviewQuestion("스프링 C", pepperLevellog, eve).getId();
            final Long question4Id = saveInterviewQuestion("스프링 D", pepperLevellog, eve).getId();

            interviewQuestionService.pressLike(question1Id, getLoginStatus(eve)); // 업데이트를 해도 최신순 정렬이 유지되어야 함

            // when
            final InterviewQuestionSearchQueryResults actual = interviewQuestionService.searchByKeyword(
                    "스프링", getLoginStatus(eve), size, page, sort);

            // then
            assertAll(
                    () -> assertThat(actual.getResults()).hasSize(4),
                    () -> Assertions.assertThat(actual.getResults())
                            .extracting(InterviewQuestionSearchQueryResult::getId)
                            .containsExactly(question4Id, question3Id, question2Id, question1Id)
            );
        }

        @Test
        @DisplayName("인터뷰 질문을 좋아요순으로 정렬하여 조회한다. ( 동률일 시 최신순으로 정렬된다. )")
        void searchByKeyword_likesSort_success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            final Long size = 10L;
            final Long page = 0L;
            final String sort = "likes";

            timeStandard.setInProgress();

            final Long question1Id = saveInterviewQuestion("스프링 A", pepperLevellog, eve).getId();
            final Long question2Id = saveInterviewQuestion("스프링 B", pepperLevellog, eve).getId();
            final Long question3Id = saveInterviewQuestion("스프링 C", pepperLevellog, eve).getId();
            final Long question4Id = saveInterviewQuestion("스프링 D", pepperLevellog, eve).getId();

            interviewQuestionService.pressLike(question1Id, getLoginStatus(eve));
            interviewQuestionService.pressLike(question1Id, getLoginStatus(pepper));
            interviewQuestionService.pressLike(question2Id, getLoginStatus(eve));

            // when
            final InterviewQuestionSearchQueryResults actual = interviewQuestionService.searchByKeyword(
                    "스프링", getLoginStatus(eve), size, page, sort);

            // then
            assertAll(
                    () -> assertThat(actual.getResults()).hasSize(4),
                    () -> Assertions.assertThat(actual.getResults())
                            .extracting(InterviewQuestionSearchQueryResult::getId)
                            .containsExactly(question1Id, question2Id, question3Id, question4Id)
            );
        }
    }

    @Nested
    @DisplayName("pressLike 메서드는")
    class PressLike {

        @Test
        @DisplayName("인터뷰 질문에 좋아요를 누른다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            timeStandard.setInProgress();

            final InterviewQuestion interviewQuestion = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);

            // when
            interviewQuestionService.pressLike(interviewQuestion.getId(), getLoginStatus(eve));

            // then
            final InterviewQuestionLikes interviewQuestionLikes = interviewQuestionLikesRepository
                    .findByInterviewQuestionIdAndLikerId(interviewQuestion.getId(), eve.getId())
                    .orElseThrow();

            assertAll(
                    () -> assertThat(interviewQuestionLikes.getInterviewQuestionId()).isEqualTo(
                            interviewQuestion.getId()),
                    () -> assertThat(interviewQuestionLikes.getLikerId()).isEqualTo(eve.getId()),
                    () -> assertThat(interviewQuestion.getLikeCount()).isEqualTo(1)
            );
        }

        @Test
        @DisplayName("이미 좋아요를 누른 상태에서 좋아요를 누르는 경우 예외를 던진다.")
        void pressLike_alreadyExist_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            timeStandard.setInProgress();

            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            interviewQuestionService.pressLike(interviewQuestionId, getLoginStatus(eve));

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.pressLike(interviewQuestionId, getLoginStatus(eve)))
                    .isInstanceOf(InterviewQuestionLikesAlreadyExistException.class)
                    .hasMessageContainingAll("인터뷰 질문에 대한 좋아요가 이미 존재합니다.",
                            String.valueOf(interviewQuestionId),
                            String.valueOf(eve.getId()));
        }
    }

    @Nested
    @DisplayName("cancelLike 메서드는")
    class CancelLike {

        @Test
        @DisplayName("인터뷰 질문에 좋아요를 취소한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            timeStandard.setInProgress();

            final InterviewQuestion interviewQuestion = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);
            interviewQuestionService.pressLike(interviewQuestion.getId(), getLoginStatus(eve));

            // when
            interviewQuestionService.cancelLike(interviewQuestion.getId(), getLoginStatus(eve));

            // then
            final Optional<InterviewQuestionLikes> interviewQuestionLikes = interviewQuestionLikesRepository
                    .findByInterviewQuestionIdAndLikerId(interviewQuestion.getId(), eve.getId());

            assertAll(
                    () -> assertThat(interviewQuestionLikes).isNotPresent(),
                    () -> assertThat(interviewQuestion.getLikeCount()).isZero()
            );
        }

        @Test
        @DisplayName("좋아요를 누르지 않았는데 좋아요를 취소하는 경우 예외를 던진다.")
        void cancelLike_notFoundInterviewQuestionLikes_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);

            timeStandard.setInProgress();

            final InterviewQuestion interviewQuestion = saveInterviewQuestion("스프링이란?", pepperLevellog, eve);

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.cancelLike(interviewQuestion.getId(), getLoginStatus(eve)))
                    .isInstanceOf(InterviewQuestionLikeNotFoundException.class)
                    .hasMessageContainingAll("인터뷰 질문을 '좋아요'하지 않았습니다.",
                            String.valueOf(interviewQuestion.getId()),
                            String.valueOf(eve.getId()));
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("업데이트된 질문 내용");

            timeStandard.setInProgress();

            // when
            interviewQuestionService.update(request, interviewQuestionId, getLoginStatus(eve));

            // then
            final List<String> actualInterviewQuestions = interviewQuestionRepository
                    .findAllByLevellogAndAuthorId(pepperLevellog, eve.getId())
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("업데이트된 질문 내용");
            final Long invalidInterviewQuestionId = 1000L;

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.update(request, invalidInterviewQuestionId, getLoginStatus(eve)))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("인터뷰 질문이 존재하지 않습니다.", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void update_unauthorized_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Member rick = saveMember("릭");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("업데이트된 질문 내용");

            timeStandard.setInProgress();

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.update(request, interviewQuestionId, getLoginStatus(rick)))
                    .isInstanceOf(MemberNotAuthorException.class)
                    .hasMessageContaining("작성자가 아닙니다.");
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("업데이트된 질문 내용");

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, getLoginStatus(eve)))
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
            final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("업데이트된 질문 내용");

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.update(request, interviewQuestionId, getLoginStatus(eve)))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContainingAll("인터뷰 진행중인 상태가 아닙니다.");
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
            interviewQuestionService.deleteById(interviewQuestionId, getLoginStatus(eve));

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

            // when & then
            assertThatThrownBy(
                    () -> interviewQuestionService.deleteById(invalidInterviewQuestionId, getLoginStatus(eve)))
                    .isInstanceOf(InterviewQuestionNotFoundException.class)
                    .hasMessageContainingAll("인터뷰 질문이 존재하지 않습니다", String.valueOf(invalidInterviewQuestionId));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void deleteById_unauthorized_exception() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Member rick = saveMember("릭");
            final Team team = saveTeam(pepper, eve);
            final Levellog pepperLevellog = saveLevellog(pepper, team);
            final Long interviewQuestionId = saveInterviewQuestion("스프링이란?", pepperLevellog, eve).getId();

            // when & then
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, getLoginStatus(rick)))
                    .isInstanceOf(MemberNotAuthorException.class)
                    .hasMessageContaining("작성자가 아닙니다.");
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
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, getLoginStatus(eve)))
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
            assertThatThrownBy(() -> interviewQuestionService.deleteById(interviewQuestionId, getLoginStatus(eve)))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContainingAll("인터뷰 진행중인 상태가 아닙니다.");
        }
    }
}
