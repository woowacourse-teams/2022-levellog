package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackRequest;
import com.woowacourse.levellog.feedback.dto.FeedbackResponse;
import com.woowacourse.levellog.feedback.dto.FeedbacksResponse;
import com.woowacourse.levellog.feedback.exception.FeedbackAlreadyExistException;
import com.woowacourse.levellog.feedback.exception.InvalidFeedbackException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
@DisplayName("FeedbackService의")
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LevellogRepository levellogRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    @DisplayName("findAll 메서드는 모든 피드백을 조회한다.")
    void findAll() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
        feedbackRepository.save(new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));

        // when
        final FeedbacksResponse feedbacksResponse = feedbackService.findAll(levellog.getId());

        // then
        assertThat(feedbacksResponse.getFeedbacks()).hasSize(2);
    }

    @Test
    @DisplayName("findAllByTo 메서드는 요청된 member가 to인 모든 피드백을 조회한다.")
    void findAllByTo() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
        final Feedback feedback1 = feedbackRepository.save(
                new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
        feedbackRepository.save(new Feedback(eve, roma, levellog, "이브 스터디", "이브 말하기", "이브 기타"));

        feedbackRepository.flush();

        // when
        feedbackService.update(feedback1.getId(),
                new FeedbackRequest(new FeedbackContentDto("update", "update", "update")));
        final List<String> fromNicknames = feedbackService.findAllByTo(eve.getId()).getFeedbacks().stream()
                .map(FeedbackResponse::getFrom).map(MemberDto::getNickname).collect(Collectors.toList());

        // then
        assertThat(fromNicknames).containsExactly("로마", "알린");
    }

    @Test
    @DisplayName("update 메서드는 피드백을 수정한다.")
    void update() {
        // given
        final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
        final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
        final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
        final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
        final Feedback feedback1 = feedbackRepository.save(
                new Feedback(roma, eve, levellog, "로마 스터디", "로마 말하기", "로마 기타"));
        feedbackRepository.save(new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));

        // when
        feedbackService.update(feedback1.getId(),
                new FeedbackRequest(new FeedbackContentDto("수정된 알린 스터디", "수정된 알린 말하기", "수정된 알린 기타")));

        // then
        final Feedback feedback = feedbackRepository.findById(feedback1.getId()).get();
        assertAll(() -> assertThat(feedback.getStudy()).contains("수정된"),
                () -> assertThat(feedback.getSpeak()).contains("수정된"),
                () -> assertThat(feedback.getEtc()).contains("수정된"));
    }

    @Nested
    @DisplayName("delete 메서드는")
    class delete {

        @Test
        @DisplayName("자신이 남긴 피드백을 삭제한다.")
        void delete_fromEqualsMe_success() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

            final Feedback alienFeedback = feedbackRepository.save(
                    new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
            final Feedback savedFeedback = feedbackRepository.save(alienFeedback);
            final Long id = savedFeedback.getId();

            // when
            feedbackService.deleteById(id, alien.getId());

            // then
            final Optional<Feedback> deletedFeedback = feedbackRepository.findById(id);
            assertThat(deletedFeedback).isEmpty();
        }

        @Test
        @DisplayName("자신이 받은 남긴 피드백을 삭제한다.")
        void delete_toEqualsMe_success() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

            final Feedback alienFeedback = feedbackRepository.save(
                    new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
            final Feedback savedFeedback = feedbackRepository.save(alienFeedback);
            final Long id = savedFeedback.getId();

            // when
            feedbackService.deleteById(id, eve.getId());

            // then
            final Optional<Feedback> deletedFeedback = feedbackRepository.findById(id);
            assertThat(deletedFeedback).isEmpty();
        }

        @Test
        @DisplayName("피드백에 관련이 없는 멤버가 삭제하면 예외를 던진다.")
        void delete_otherMember_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

            final Feedback alienFeedback = feedbackRepository.save(
                    new Feedback(alien, eve, levellog, "알린 스터디", "알린 말하기", "알린 기타"));
            final Feedback savedFeedback = feedbackRepository.save(alienFeedback);
            final Long id = savedFeedback.getId();

            // when, then
            assertThatThrownBy(() -> feedbackService.deleteById(id, roma.getId())).isInstanceOf(
                    InvalidFeedbackException.class).hasMessage("자신이 남기거나 받은 피드백만 삭제할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("save 메서드는")
    class save {

        @Test
        @DisplayName("피드백을 저장한다.")
        void save() {
            // given
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                    "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, roma, true));

            // when
            final Long id = feedbackService.save(levellog.getId(), roma.getId(), request);

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
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));

            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));
            feedbackRepository.save(new Feedback(roma, eve, levellog, "study", "speak", "etc"));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                    "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(levellog.getId(), roma.getId(), request)).isInstanceOf(
                    FeedbackAlreadyExistException.class);
        }

        @Test
        @DisplayName("작성자가 직접 피드백을 작성하면 예외를 던진다.")
        void save_selfFeedback_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            participantRepository.save(new Participant(team, eve, true));
            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                    "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
            final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);

            // when, then
            assertThatThrownBy(() -> feedbackService.save(levellog.getId(), eve.getId(), request)).isInstanceOf(
                    InvalidFeedbackException.class).hasMessage("자기 자신에게 피드백을 할 수 없습니다.");
        }

        @Test
        @DisplayName("팀에 속하지 않은 멤버가 피드백을 작성할 경우 예외를 발생시킨다.")
        void save_otherMember_exceptionThrown() {
            // given
            final Member eve = memberRepository.save(new Member("이브", 1111, "eve.img"));
            final Member roma = memberRepository.save(new Member("로마", 2222, "roma.img"));
            final Member alien = memberRepository.save(new Member("알린", 3333, "alien.img"));

            final Team team = teamRepository.save(
                    new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "progile.img"));
            participantRepository.save(new Participant(team, eve, true));
            participantRepository.save(new Participant(team, alien, true));

            final Levellog levellog = levellogRepository.save(new Levellog(eve, team, "이브의 레벨로그"));

            // when, then
            assertThatThrownBy(() -> feedbackService.save(levellog.getId(), roma.getId(),
                    new FeedbackRequest(new FeedbackContentDto("로마 스터디", "로마 말하기", "로마 기타"))))
                    .isInstanceOf(InvalidFeedbackException.class);
        }
    }
}
