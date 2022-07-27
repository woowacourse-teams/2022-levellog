package com.woowacourse.levellog.domain;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Feedback의")
class FeedbackTest {

    @Nested
    @DisplayName("생성자는")
    class updateNickname {

        @Test
        @DisplayName("Study 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void feedback_studyLength_exceptionThrown() {
            final String feedback = "feedback".repeat(126);
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    feedback, "Speak 피드백", "Etc 피드백");

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img");
            final Levellog levellog = new Levellog(eve, team, "레벨로그 작성 내용");

            Assertions.assertThatThrownBy(() -> Feedback.of(roma, levellog, feedbackContentDto))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Speak 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void feedback_SpeakLength_exceptionThrown() {
            final String feedback = "feedback".repeat(126);
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Study 피드백", feedback, "Etc 피드백");

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img");
            final Levellog levellog = new Levellog(eve, team, "레벨로그 작성 내용");

            Assertions.assertThatThrownBy(() -> Feedback.of(roma, levellog, feedbackContentDto))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Etc 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void feedback_EtcLength_exceptionThrown() {
            final String feedback = "feedback".repeat(126);
            final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                    "Study 피드백", "Speak 피드백", feedback);

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now(), "progile.img");
            final Levellog levellog = new Levellog(eve, team, "레벨로그 작성 내용");

            Assertions.assertThatThrownBy(() -> Feedback.of(roma, levellog, feedbackContentDto))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }
    }
}