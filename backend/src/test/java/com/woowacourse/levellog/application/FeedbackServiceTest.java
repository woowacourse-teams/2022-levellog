package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FeedbackService의")
class FeedbackServiceTest extends ServiceTest {

    private Member saveAndGetMember(final String nickname) {
        return memberRepository.save(new Member(nickname, (int) System.nanoTime(), "profile.png"));
    }

    private Team saveAndGetTeam(final String title, final int interviewerNumber) {
        return teamRepository.save(
                new Team(title, "피니시방", LocalDateTime.now().plusDays(3), "jason.png", interviewerNumber));
    }

    private void saveAllParticipant(final Team team, final Member host, final Member... participants) {
        participantRepository.save(new Participant(team, host, true));
        for (final Member participant : participants) {
            participantRepository.save(new Participant(team, participant, false));
        }
    }

    @Test
    @DisplayName("findAllByTo 메서드는 요청된 member가 to인 모든 피드백을 조회한다.")
    void findAllByTo() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));
        final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
        feedbackRepository.save(new Feedback(eve, roma, levellog, "이브 스터디", "이브 말하기", "이브 기타"));

        feedbackRepository.save(new Feedback(roma, eve, levellog, "로마가 이브에게 스터디", "로마가 이브에게 말하기", "로마가 이브에 기타"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린이 이브에게 스터디", "알린이 이브에게 말하기", "알린이 이브에게 기타"));
        feedbackRepository.save(new Feedback(eve, roma, levellog, "이브가 로마에게 스터디", "이브가 로마에게 말하기", "이브가 로마에게 기타"));

        // when
        final List<String> fromNicknames = feedbackService.findAllByTo(eve.getId())
                .getFeedbacks()
                .stream()
                .map(FeedbackDto::getFrom)
                .map(MemberDto::getNickname)
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
            final Member eve = saveAndGetMember("이브");
            final Member roma = saveAndGetMember("로마");
            final Member alien = saveAndGetMember("알린");
            final Team team = saveAndGetTeam("잠실 네오조", 1);
            saveAllParticipant(team, eve, roma);
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));
            saveAllParticipant(team, eve, roma, alien);
            feedbackRepository.save(new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
            feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));

            // when
            final FeedbacksDto feedbacksResponse = feedbackService.findAll(levellog.getId(), eve.getId());

            // then
            assertThat(feedbacksResponse.getFeedbacks()).hasSize(2);
        }

        @Test
        @DisplayName("속하지 않은 팀의 피드백 조회를 요청하면 예외가 발생한다.")
        void findAll_notMyTeam_exception() {
            // given
            final Member eve = saveAndGetMember("이브");
            final Member roma = saveAndGetMember("로마");
            final Member alien = saveAndGetMember("알린");
            final Team team = saveAndGetTeam("잠실 네오조", 1);
            saveAllParticipant(team, eve, roma);
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));
            feedbackRepository.save(new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));

            // when & then
            final Long memberId = alien.getId();
            final Long levellogId = levellog.getId();
            assertThatThrownBy(() -> feedbackService.findAll(levellogId, memberId))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("자신이 속한 팀의 피드백만 조회할 수 있습니다.", String.valueOf(team.getId()),
                            String.valueOf(memberId), String.valueOf(levellogId));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        @Test
        @DisplayName("피드백을 수정한다.")
        void update_feedback_success() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "profile.img", 1));
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final Feedback feedback1 = feedbackRepository.save(
                    new Feedback(roma, eve, levellog, "로마가 이브에게 스터디", "로마가 이브에게 말하기", "로마가 이브에게 기타"));

            // when
            feedbackService.update(new FeedbackWriteDto(
                            new FeedbackContentDto("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타")),
                    feedback1.getId(), roma.getId());

            // then
            final Feedback feedback = feedbackRepository.findById(feedback1.getId()).get();
            assertAll(() -> assertThat(feedback.getStudy()).contains("수정된"),
                    () -> assertThat(feedback.getSpeak()).contains("수정된"),
                    () -> assertThat(feedback.getEtc()).contains("수정된"));
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 피드백을 수정하려는 경우 예외를 발생시킨다.")
        void update_noAuthor_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final Feedback feedback1 = feedbackRepository.save(
                    new Feedback(roma, eve, levellog, "로마가 이브에게 스터디", "로마가 이브에게 말하기", "로마가 이브에게 기타"));

            // when, then
            assertThatThrownBy(() ->
                    feedbackService.update(new FeedbackWriteDto(
                                    new FeedbackContentDto("수정된 스터디", "수정된 말하기", "수정된 기타")),
                            feedback1.getId(), alien.getId()))
                    .isInstanceOf(InvalidFeedbackException.class)
                    .hasMessageContaining("자신이 남긴 피드백만 수정할 수 있습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 시간 이전에 피드백을 수정하려는 경우 예외가 발생한다.")
        void update_beforeStartAt_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(10), "profile.img", 1));
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final Feedback feedback1 = feedbackRepository.save(
                    new Feedback(roma, eve, levellog, "로마가 이브에게 스터디", "로마가 이브에게 말하기", "로마가 이브에게 기타"));

            // when & then
            assertThatThrownBy(() -> feedbackService.update(new FeedbackWriteDto(
                            new FeedbackContentDto("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타")),
                    feedback1.getId(), roma.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 종료 이후에 피드백을 수정하려는 경우 예외가 발생한다.")
        void update_alreadyClosed_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));

            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", startAt, "profile.img", 1));

            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final Feedback feedback1 = feedbackRepository.save(
                    new Feedback(roma, eve, levellog, "로마가 이브에게 스터디", "로마가 이브에게 말하기", "로마가 이브에게 기타"));

            team.close(startAt.plusDays(1));

            // when & then
            assertThatThrownBy(() -> feedbackService.update(new FeedbackWriteDto(
                            new FeedbackContentDto("수정된 로마가 이브에게 스터디", "수정된 로마가 이브에게 말하기", "수정된 로마가 이브에게 기타")),
                    feedback1.getId(), roma.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("이미 종료된 인터뷰입니다.");
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("피드백을 저장한다.")
        void save_feedback_success() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, roma, false));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            // when
            final Long id = feedbackService.save(request, levellog.getId(), roma.getId());

            // then
            final Optional<Feedback> feedback = feedbackRepository.findById(id);
            assertThat(feedback).isPresent();
        }

        @Test
        @DisplayName("레벨로그에 내가 작성한 피드백이 이미 존재하는 경우 새로운 피드백을 작성하면 예외를 던진다.")
        void save_alreadyExist_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));

            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));

            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));
            feedbackRepository.save(new Feedback(roma, eve, levellog, "study", "speak", "etc"));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), roma.getId()))
                    .isInstanceOf(FeedbackAlreadyExistException.class)
                    .hasMessageContaining("피드백이 이미 존재합니다. levellogId : " + levellog.getId());
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));
            participantRepository.save(new Participant(team, eve, true));
            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), eve.getId()))
                    .isInstanceOf(InvalidFeedbackException.class)
                    .hasMessageContaining("자기 자신에게 피드백을 할 수 없습니다.");
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));

            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img", 1));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, alien, false));

            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            // when, then
            assertThatThrownBy(() -> feedbackService.save(new FeedbackWriteDto(
                            new FeedbackContentDto("로마 스터디", "로마 말하기", "로마 기타")),
                    levellog.getId(), roma.getId()))
                    .isInstanceOf(InvalidFeedbackException.class)
                    .hasMessageContaining("같은 팀에 속한 멤버만 피드백을 작성할 수 있습니다.");
        }

        @Test
        @DisplayName("인터뷰 시작 전 피드백을 작성하면 예외를 던진다.")
        void save_beforeStartAt_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));

            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(10), "progile.img", 1));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, roma, false));

            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), roma.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("인터뷰가 시작되기 전에 피드백을 작성 또는 수정할 수 없습니다.");
        }

        @Test
        @DisplayName("인터뷰 종료 후 피드백을 작성하면 예외를 던진다.")
        void save_alreadyClosed_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));

            final LocalDateTime startAt = LocalDateTime.now().plusDays(3);
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", startAt, "progile.img", 1));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, roma, false));

            final Levellog levellog = levellogRepository.save(Levellog.of(eve, team, "이브의 레벨로그"));

            team.close(startAt.plusDays(1));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackWriteDto request = new FeedbackWriteDto(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(request, levellog.getId(), roma.getId()))
                    .isInstanceOf(InterviewTimeException.class)
                    .hasMessageContaining("이미 종료된 인터뷰입니다.");
        }
    }
}
