package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.dto.request.FeedbackContentRequest;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Feedback의")
class FeedbackTest {

    @Nested
    @DisplayName("생성자는")
    class Create {

        @Test
        @DisplayName("Study 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void create_feedbackStudyLength_exception() {
            // given
            final String feedback = "f".repeat(1001);
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    feedback, "Speak 피드백", "Etc 피드백");

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            // when & then
            assertThatThrownBy(() -> feedbackContentRequest.toEntity(roma.getId(), levellog))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Speak 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void create_feedbackSpeakLength_exception() {
            // given
            final String feedback = "f".repeat(1001);
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    "Study 피드백", feedback, "Etc 피드백");

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            // when & then
            assertThatThrownBy(() -> feedbackContentRequest.toEntity(roma.getId(), levellog))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Etc 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void create_feedbackEtcLength_exception() {
            // given
            final String feedback = "f".repeat(1001);
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    "Study 피드백", "Speak 피드백", feedback);

            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            // when & then
            assertThatThrownBy(() -> feedbackContentRequest.toEntity(roma.getId(), levellog))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }
    }

    @Nested
    @DisplayName("updateFeedback 메소드는")
    class Update {

        @Test
        @DisplayName("Study 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void update_studyLength_exception() {
            // given
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    "Study 피드백", "Speak 피드백", "Etc 피드백");
            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            final Feedback feedback = feedbackContentRequest.toEntity(roma.getId(), levellog);

            final String studyFeedback = "f".repeat(1001);

            // when & then
            assertThatThrownBy(() -> feedback.updateFeedback(studyFeedback, "Speak 피드백", "Etc 피드백"))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Speak 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void update_speakLength_exception() {
            // given
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    "Study 피드백", "Speak 피드백", "Etc 피드백");
            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            final Feedback feedback = feedbackContentRequest.toEntity(roma.getId(), levellog);

            final String speakFeedback = "f".repeat(1001);

            // when & then
            assertThatThrownBy(() -> feedback.updateFeedback("Study 피드백", speakFeedback, "Etc 피드백"))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }

        @Test
        @DisplayName("Etc 피드백의 길이가 1000자 초과일 경우 예외를 발생시킨다.")
        void update_etcLength_exception() {
            // given
            final FeedbackContentRequest feedbackContentRequest = new FeedbackContentRequest(
                    "Study 피드백", "Speak 피드백", "Etc 피드백");
            final Member roma = new Member("로마", 123456, "image.png");
            final Member eve = new Member("이브", 123123, "image.png");
            final Team team = TeamTest.saveTeam();
            final Levellog levellog = new Levellog(eve.getId(), team, "레벨로그 작성 내용");

            final Feedback feedback = feedbackContentRequest.toEntity(roma.getId(), levellog);

            final String etcFeedback = "f".repeat(1001);

            // when & then
            assertThatThrownBy(() -> feedback.updateFeedback("Study 피드백", "Speak 피드백", etcFeedback))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("피드백은 1000자 이하여야 합니다.");
        }
    }
}
