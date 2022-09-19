package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionLikesRepository의")
class InterviewQuestionLikesRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("existsByInterviewQuestionIdAndLikerId 메서드는 interviewQuestionId와 likerId가 모두 일치하는 인터뷰 질문이 있는지 확인하여 반환한다..")
    void existsByInterviewQuestionIdAndLikerId() {
        // given
        final Member eve = saveMember("eve");
        final Member toMember = saveMember("toMember");

        final Team team = saveTeam(eve, toMember);
        final Levellog levellog = saveLevellog(toMember, team);

        final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?", levellog, eve);
        final InterviewQuestion savedInterviewQuestion2 = saveInterviewQuestion("AOP란?", levellog, eve);

        pressLikeInterviewQuestion(savedInterviewQuestion1, eve);

        // when
        final boolean actual = interviewQuestionLikesRepository.existsByInterviewQuestionIdAndLikerId(
                savedInterviewQuestion1.getId(), eve.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("findByInterviewQuestionIdAndLikerId 메서드는 interviewQuestionId와 likerId가 모두 일치하는 인터뷰 질문을 반환한다.")
    void findByInterviewQuestionIdAndLikerId() {
        // given
        final Member eve = saveMember("eve");
        final Member toMember = saveMember("toMember");

        final Team team = saveTeam(eve, toMember);
        final Levellog levellog = saveLevellog(toMember, team);

        final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?", levellog, eve);
        final InterviewQuestion savedInterviewQuestion2 = saveInterviewQuestion("AOP란?", levellog, eve);

        pressLikeInterviewQuestion(savedInterviewQuestion1, eve);

        // when
        final Optional<InterviewQuestionLikes> actual = interviewQuestionLikesRepository.findByInterviewQuestionIdAndLikerId(
                savedInterviewQuestion1.getId(), eve.getId());

        // then
        assertThat(actual).isPresent();
    }
}
