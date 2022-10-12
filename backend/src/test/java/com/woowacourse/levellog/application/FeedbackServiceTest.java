package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.AFTER_START_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.dto.request.FeedbackWriteRequest;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponse;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.NotParticipantException;
import com.woowacourse.levellog.team.exception.TeamAlreadyClosedException;
import com.woowacourse.levellog.team.exception.TeamNotInProgressException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FeedbackService의")
class FeedbackServiceTest extends ServiceTest {

    @Test
    @DisplayName("findAllByTo 메서드는 요청된 member가 to인 모든 피드백을 조회한다.")
    void findAllByTo() {
        // given
        final Member eve = saveMember("이브");
        final Member roma = saveMember("로마");
        final Member alien = saveMember("알린");
        final Team team = saveTeam(eve, roma, alien);
        final Levellog levellog = saveLevellog(eve, team);

        saveFeedback(alien, eve, levellog);
        saveFeedback(eve, roma, levellog);

        saveFeedback(roma, eve, levellog);
        saveFeedback(alien, eve, levellog);
        saveFeedback(eve, roma, levellog);

        // when
        final List<String> fromNicknames = feedbackService.findAllByTo(getLoginStatus(eve))
                .getFeedbacks()
                .stream()
                .map(FeedbackResponse::getFrom)
                .map(MemberResponse::getNickname)
                .collect(Collectors.toList());

        // then
        assertThat(fromNicknames).contains("로마", "알린");
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("모든 피드백을 조회한다.")
        void success() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(eve, roma, alien);

            final Levellog levellog = saveLevellog(eve, team);
            saveFeedback(roma, eve, levellog);
            saveFeedback(alien, eve, levellog);

            // when
            final FeedbackResponses feedbackResponses = feedbackService.findAll(levellog.getId(),
                    getLoginStatus(eve));

            // then
            assertThat(feedbackResponses.getFeedbacks()).hasSize(2);
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);
            saveFeedback(roma, eve, levellog);

            final Long levellogId = levellog.getId();

            // when & then
            assertThatThrownBy(() -> feedbackService.findAll(levellogId, getLoginStatus(alien)))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContainingAll("팀 참가자가 아닙니다.",
                            String.valueOf(team.getId()), String.valueOf(alien.getId()));
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("특정 id의 피드백을 조회한다.")
        void success() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(eve, roma, alien);

            final Levellog levellog = saveLevellog(eve, team);
            final Feedback feedback = saveFeedback(roma, eve, levellog);
            saveFeedback(alien, eve, levellog);

            // when
            final FeedbackResponse feedbacksResponse = feedbackService.findById(
                    levellog.getId(), feedback.getId(), getLoginStatus(eve));

            // then
            assertAll(
                    () -> assertThat(feedbacksResponse.getId()).isEqualTo(feedback.getId()),
                    () -> assertThat(feedbacksResponse.getFrom().getId()).isEqualTo(roma.getId()),
                    () -> assertThat(feedbacksResponse.getTo().getId()).isEqualTo(eve.getId())
            );
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findById_notMyTeam_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);
            final Feedback feedback = saveFeedback(roma, eve, levellog);

            final Long levellogId = levellog.getId();

            // when & then
            assertThatThrownBy(() -> feedbackService.findById(levellogId, feedback.getId(), getLoginStatus(alien)))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContainingAll("팀 참가자가 아닙니다.",
                            String.valueOf(team.getId()), String.valueOf(alien.getId()));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("피드백을 수정한다.")
        void success() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final Long feedbackId = saveFeedback(roma, eve, levellog).getId();

            timeStandard.setInProgress();

            // when
            feedbackService.update(new FeedbackWriteRequest("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타"),
                    feedbackId, getLoginStatus(roma));

            // then
            final Feedback feedback = feedbackRepository.findById(feedbackId).get();
            assertAll(() -> assertThat(feedback.getStudy()).contains("수정된"),
                    () -> assertThat(feedback.getSpeak()).contains("수정된"),
                    () -> assertThat(feedback.getEtc()).contains("수정된"));
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 피드백을 수정하려는 경우 예외를 발생시킨다.")
        void update_noAuthor_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");
            final Team team = saveTeam(eve, roma, alien);
            final Levellog levellog = saveLevellog(eve, team);

            final Long feedbackId = saveFeedback(roma, eve, levellog).getId();

            // when, then
            assertThatThrownBy(() ->
                    feedbackService.update(new FeedbackWriteRequest("수정된 스터디", "수정된 말하기", "수정된 기타"),
                            feedbackId, getLoginStatus(alien)))
                    .isInstanceOf(InvalidFeedbackException.class)
                    .hasMessageContaining("잘못된 피드백 요청입니다.");
        }

        @Test
        @DisplayName("진행 상태가 아닐 때 피드백을 수정하려는 경우 예외가 발생한다.")
        void update_notInProgress_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final Long feedbackId = saveFeedback(roma, eve, levellog).getId();

            // when & then
            assertThatThrownBy(() -> feedbackService.update(
                    new FeedbackWriteRequest("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타"),
                    feedbackId, getLoginStatus(roma)))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContaining("인터뷰 진행중인 상태가 아닙니다.");
        }

        @Test
        @DisplayName("인터뷰 종료 이후에 피드백을 수정하려는 경우 예외가 발생한다.")
        void update_alreadyClosed_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final Long feedbackId = saveFeedback(roma, eve, levellog).getId();

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            // when & then
            assertThatThrownBy(() -> feedbackService.update(
                    new FeedbackWriteRequest("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타"),
                    feedbackId, getLoginStatus(roma)))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("피드백을 저장한다.")
        void success() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            timeStandard.setInProgress();

            // when
            final Long id = feedbackService.save(request, levellog.getId(), getLoginStatus(roma));

            // then
            final Optional<Feedback> feedback = feedbackRepository.findById(id);
            assertThat(feedback).isPresent();
        }

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(eve, roma);

            final Levellog levellog = saveLevellog(eve, team);
            saveFeedback(roma, eve, levellog);

            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), getLoginStatus(roma)))
                    .isInstanceOf(FeedbackAlreadyExistException.class)
                    .hasMessageContainingAll("피드백이 이미 존재합니다.", levellog.getId().toString());
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), getLoginStatus(eve)))
                    .isInstanceOf(InvalidFeedbackException.class)
                    .hasMessageContaining("잘못된 피드백 요청입니다.");
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");
            final Member alien = saveMember("알린");

            final Team team = saveTeam(eve, alien);
            final Levellog levellog = saveLevellog(eve, team);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(new FeedbackWriteRequest("로마 스터디", "로마 말하기", "로마 기타"),
                    levellog.getId(), getLoginStatus(roma)))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContaining("팀 참가자가 아닙니다.");
        }

        @Test
        @DisplayName("팀 진행 상태가 아닐 때 피드백을 작성하면 예외를 던진다.")
        void save_notInProgress_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), getLoginStatus(roma)))
                    .isInstanceOf(TeamNotInProgressException.class)
                    .hasMessageContaining("인터뷰 진행중인 상태가 아닙니다.");
        }

        @Test
        @DisplayName("인터뷰 종료 후 피드백을 작성하면 예외를 던진다.")
        void save_alreadyClosed_exception() {
            // given
            final Member eve = saveMember("이브");
            final Member roma = saveMember("로마");

            final Team team = saveTeam(eve, roma);
            final Levellog levellog = saveLevellog(eve, team);

            timeStandard.setInProgress();
            team.close(AFTER_START_TIME);

            final FeedbackWriteRequest request = new FeedbackWriteRequest(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), getLoginStatus(roma)))
                    .isInstanceOf(TeamAlreadyClosedException.class)
                    .hasMessageContaining("이미 인터뷰가 종료된 팀입니다.");
        }
    }
}
