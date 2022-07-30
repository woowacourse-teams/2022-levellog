package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.interview_question.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestion의")
class InterviewQuestionTest {

    @Nested
    @DisplayName("생성자는")
    class ConstructorTest {

        @Test
        @DisplayName("인터뷰 질문을 생성한다.")
        void success() {
            // given
            final Member from = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "레벨로그 작성 내용");
            final String content = "스프링이란?";

            // when & then
            assertDoesNotThrow(() -> InterviewQuestion.of(from, to, levellog, content));
        }

        @Test
        @DisplayName("인터뷰 질문의 길이가 255자 초과일 경우 예외를 발생시킨다.")
        void interviewQuestion_contentLength_exception() {
            // given
            final Member from = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "스프링을 공부하였습니다.");
            final String content = "스프링이란?".repeat(255);

            // when & then
            assertThatThrownBy(() -> InterviewQuestion.of(from, to, levellog, content))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("인터뷰 질문은 255자 이하여야합니다.", String.valueOf(content.length()));
        }
    }
}
