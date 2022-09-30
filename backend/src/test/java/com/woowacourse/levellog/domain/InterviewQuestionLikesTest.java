package com.woowacourse.levellog.domain;

import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
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
        final Team team = new Team("잠실 네오조", "트랙룸", TEAM_START_TIME, "jamsil.img", 1);
        final Levellog levellog = Levellog.of(to.getId(), team, "레벨로그 작성 내용");
        final String content = "스프링이란?";
        final InterviewQuestion interviewQuestion = InterviewQuestion.of(author.getId(), levellog, content);

        // when & then
        assertDoesNotThrow(
                () -> InterviewQuestionLikes.of(interviewQuestion, author));
    }
}
