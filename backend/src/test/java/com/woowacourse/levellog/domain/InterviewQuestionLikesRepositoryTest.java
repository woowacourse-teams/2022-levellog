package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.StringConverter;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionSort;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResult;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("searchByKeyword 메서드는")
    class SearchByKeyword {

        @Test
        @DisplayName("키워드가 포함된 인터뷰 질문을 반환한다.")
        void success() {
            // given
            final Member eve = saveMember("eve");
            final Member toMember = saveMember("toMember");

            final Team team = saveTeam(eve, toMember);
            final Levellog levellog = saveLevellog(toMember, team);

            final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?1", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?2", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?3", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?4", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?5", levellog, eve);
            saveInterviewQuestion("AOP란?", levellog, eve);

            pressLikeInterviewQuestion(savedInterviewQuestion1, eve);

            // when
            final List<InterviewQuestionSearchQueryResult> actual = interviewQuestionQueryRepository.searchByKeyword(
                    "스프링을 왜 사용하였나요?", getLoginStatus(eve), 10L, 0L, InterviewQuestionSort.LATEST
            );

            // then
            assertThat(actual).hasSize(5);
            assertThat(actual.get(0).getContent()).isEqualTo("스프링을 왜 사용하였나요?5");
        }

        @Test
        @DisplayName("로그인하지 않은 경우 더 간결한 쿼리를 날린다.")
        void success_notLogin() {
            // given
            final Member eve = saveMember("eve");
            final Member toMember = saveMember("toMember");

            final Team team = saveTeam(eve, toMember);
            final Levellog levellog = saveLevellog(toMember, team);

            final InterviewQuestion savedInterviewQuestion1 = saveInterviewQuestion("스프링을 왜 사용하였나요?1", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?2", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?3", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?4", levellog, eve);
            saveInterviewQuestion("스프링을 왜 사용하였나요?5", levellog, eve);
            saveInterviewQuestion("AOP란?", levellog, eve);

            pressLikeInterviewQuestion(savedInterviewQuestion1, eve);

            // when
            final List<InterviewQuestionSearchQueryResult> actual = interviewQuestionQueryRepository.searchByKeyword(
                    "스프링을 왜 사용하였나요?", LoginStatus.fromNotLogin(), 10L, 0L, InterviewQuestionSort.LATEST
            );

            // then
            assertThat(actual).hasSize(5)
                    .extracting("like", Boolean.class)
                    .containsExactly(false, false, false, false, false);
        }

        @Test
        @DisplayName("sql injection 위험이 있는 키워드로 인터뷰 질문 조회 시 0개를 반환한다.")
        void success_sql_injection() {
            // given
            final Member eve = saveMember("eve");
            final Member toMember = saveMember("toMember");

            final Team team = saveTeam(eve, toMember);
            final Levellog levellog = saveLevellog(toMember, team);

            saveInterviewQuestion("스프링을 왜 사용하였나요?", levellog, eve);
            saveInterviewQuestion("트랜잭션을 왜 사용하였나요?", levellog, eve);

            // when
            final String sqlInjectionKeyword = "왜%' or 1=1;--";
            final String safeKeyword = StringConverter.toSafeString(sqlInjectionKeyword);
            final List<InterviewQuestionSearchQueryResult> actual = interviewQuestionQueryRepository.searchByKeyword(
                    safeKeyword, getLoginStatus(eve), 10L, 0L, InterviewQuestionSort.LATEST
            );

            // then
            assertThat(actual).isEmpty();
        }
    }
}
