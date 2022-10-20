package com.woowacourse.levellog.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionLikes 클래스의")
class InterviewQuestionLikesTest {

    @Test
    @DisplayName("생성자는 인터뷰 질문 좋아요 내역을 생성한다.")
    void success() {
        // given
        final Member author = new Member("페퍼", 1111, "pepper.png");
        final Member to = new Member("이브", 123123, "image.png");
        final Team team = TeamTest.saveTeam();
        final Levellog levellog = new Levellog(to.getId(), team, "레벨로그 작성 내용");
        final String content = "스프링이란?";
        final InterviewQuestion interviewQuestion = new InterviewQuestion(author.getId(), levellog, content);

        // when & then
        assertDoesNotThrow(
                () -> new InterviewQuestionLikes(interviewQuestion.getId(), author.getId()));
    }
}
