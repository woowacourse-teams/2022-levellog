package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestion 클래스의")
class InterviewQuestionTest {

    @Nested
    @DisplayName("생성자는")
    class ConstructorTest {

        @Test
        @DisplayName("인터뷰 질문을 생성한다.")
        void success() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "레벨로그 작성 내용");
            final String content = "스프링이란?";

            // when & then
            assertDoesNotThrow(() -> InterviewQuestion.of(author, levellog, content));
        }

        @Test
        @DisplayName("인터뷰 질문의 길이가 255자 초과일 경우 예외를 던진다.")
        void interviewQuestion_contentLength_exception() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "스프링을 공부하였습니다.");
            final String content = "a".repeat(256);

            // when & then
            assertThatThrownBy(() -> InterviewQuestion.of(author, levellog, content))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("인터뷰 질문은 255자 이하여야합니다.", String.valueOf(content.length()));
        }
    }

    @Nested
    @DisplayName("updateContent 메서드는")
    class UpdateContentTest {

        @Test
        @DisplayName("인터뷰 질문 내용을 수정한다.")
        void success() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "레벨로그 작성 내용");
            final InterviewQuestion interviewQuestion = InterviewQuestion.of(author, levellog, "스프링이란?");

            // when
            final String updatedContent = "스프링 빈이란?";
            interviewQuestion.updateContent(updatedContent, author);

            // then
            assertThat(interviewQuestion.getContent())
                    .isEqualTo(updatedContent);
        }

        @Test
        @DisplayName("수정하려는 인터뷰 질문의 길이가 255자 초과일 경우 예외를 던진다.")
        void updateContent_contentLength_exception() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "레벨로그 작성 내용");
            final InterviewQuestion interviewQuestion = InterviewQuestion.of(author, levellog, "스프링이란?");

            // when & then
            final String updatedContent = "a".repeat(256);
            assertThatThrownBy(() -> interviewQuestion.updateContent(updatedContent, author))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContainingAll("인터뷰 질문은 255자 이하여야합니다.", String.valueOf(updatedContent.length()));
        }

        @Test
        @DisplayName("인터뷰 질문 작성자가 아닌 경우 권한 없음 예외를 던진다.")
        void updateContent_unauthorized_exception() {
            // given
            final Member author = new Member("페퍼", 1111, "pepper.png");
            final Member to = new Member("이브", 2222, "image.png");
            final Member otherMember = new Member("릭", 123123, "image.png");
            final Team team = new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), "jamsil.img");
            final Levellog levellog = Levellog.of(to, team, "레벨로그 작성 내용");
            final InterviewQuestion interviewQuestion = InterviewQuestion.of(author, levellog, "스프링이란?");

            // when & then
            assertThatThrownBy(() -> interviewQuestion.updateContent("스프링 빈이란?", otherMember))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContainingAll("인터뷰 질문을 수정할 수 있는 권한이 없습니다.", String.valueOf(otherMember.getId()),
                            String.valueOf(author.getId()), String.valueOf(levellog.getId()));
        }
    }
}
