package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionNotFoundException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionRepository의")
class InterviewQuestionRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellogAndAuthor 메서드는 levellog와 author 멤버가 모두 일치하는 인터뷰 질문 목록를 반환한다.")
    void findAllByLevellogAndAuthor() {
        // given
        final Member eve = saveMember("eve");
        final Member toMember = saveMember("toMember");

        final Team team = saveTeam(eve, toMember);
        final Levellog levellog = saveLevellog(toMember, team);

        final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?", levellog, eve);
        final InterviewQuestion savedInterviewQuestion2 = saveInterviewQuestion("AOP란?", levellog, eve);

        // when
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthorId(
                levellog, eve.getId());

        // then
        assertThat(interviewQuestions).hasSize(2)
                .contains(savedInterviewQuestion1, savedInterviewQuestion2);
    }

    @Nested
    @DisplayName("getInterviewQuestion 메서드는")
    class GetInterviewQuestion {

        @Test
        @DisplayName("interviewQuestionId에 해당하는 레코드가 존재하면 id에 해당하는 InterviewQuestion 엔티티를 반환한다.")
        void success() {
            // given
            final Member pepper = saveMember("페퍼");
            final Member rick = saveMember("릭");

            final Team team = saveTeam(pepper, rick);
            final Levellog levellog = saveLevellog(pepper, team);
            final Long expected = saveInterviewQuestion("릭이 씀", levellog, rick).getId();

            // when
            final Long actual = interviewQuestionRepository.getInterviewQuestion(expected).getId();

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("interviewQuestionId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getInterviewQuestion_notExist_exception() {
            // given
            final Long interviewQuestionId = 999L;

            // when & then
            assertThatThrownBy(() -> interviewQuestionRepository.getInterviewQuestion(interviewQuestionId))
                    .isInstanceOf(InterviewQuestionNotFoundException.class);
        }
    }
}
