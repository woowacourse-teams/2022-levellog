package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionRepository의")
class InterviewQuestionRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellogAndAuthor 메서드는 levellog와 author 멤버가 모두 일치하는 인터뷰 질문 목록를 반환한다.")
    void findAllByLevellogAndAuthor() {
        // given
        final Member eve = saveMember("이브", "깃허브_이브");
        final Member toMember = saveMember("알린", "깃허브_알린");

        final Team team = saveTeam(eve, toMember);
        final Levellog levellog = saveLevellog(toMember, team);

        final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?", levellog, eve);
        final InterviewQuestion savedInterviewQuestion2 = saveInterviewQuestion("AOP란?", levellog, eve);

        // when
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthor(
                levellog, eve);

        // then
        assertThat(interviewQuestions).hasSize(2)
                .contains(savedInterviewQuestion1, savedInterviewQuestion2);
    }

    @Test
    @DisplayName("findAllByLevellog 메서드는 levellog가 일치하는 인터뷰 질문 목록을 반환한다.")
    void findAllByLevellog() {
        // given
        final Member pepper = saveMember("페퍼", "깃허브_페퍼");
        final Member rick = saveMember("릭", "깃허브_릭");
        final Member roma = saveMember("로마", "깃허브_로마");

        final Team team = saveTeam(pepper, rick, roma);
        final Levellog levellog = saveLevellog(pepper, team);

        final InterviewQuestion InterviewQuestion1 = saveInterviewQuestion("로마가 씀", levellog, roma);
        final InterviewQuestion InterviewQuestion2 = saveInterviewQuestion("릭이 씀", levellog, rick);
        final InterviewQuestion interviewQuestion3 = saveInterviewQuestion("로마가 씀 - 꼬리 질문", levellog, roma);

        final Levellog rickLevellog = saveLevellog(rick, team);

        saveInterviewQuestion("조회되면 안되는 인터뷰 질문 1", rickLevellog, roma);
        saveInterviewQuestion("조회되면 안되는 인터뷰 질문 2", rickLevellog, pepper);

        // when
        final List<InterviewQuestion> actual = interviewQuestionRepository.findAllByLevellog(levellog);

        // then
        assertThat(actual).hasSize(3)
                .containsExactly(InterviewQuestion1, InterviewQuestion2, interviewQuestion3);
    }
}
