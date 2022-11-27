package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.fixture.ConcurrentRequester;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResults;
import com.woowacourse.levellog.member.dto.response.MemberResponses;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("동시성 관련 기능")
class ConcurrentAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 한 명의 사용자가 동시에 회원가입
     *   when: 동시에 여러 개의 피드백 작성을 요청한다.
     *   then: 한 개의 피드백만 작성에 성공한다.
     */
    @Test
    @DisplayName("멤버 동시 가입")
    void concurrentlyCreateMember() throws InterruptedException {
        // given
        final int threadPoolSize = 2;
        final ConcurrentRequester concurrentRequester = new ConcurrentRequester(threadPoolSize);

        // when
        for (int i = 0; i < threadPoolSize; i++) {
            concurrentRequester.execute(PEPPER::login);
        }
        concurrentRequester.await();

        // then
        final int numberOfSavedMember = getResponse("/api/members?nickname=페퍼", MemberResponses.class)
                .getMembers()
                .size();

        assertAll(
                () -> assertThat(concurrentRequester.getSuccessRequest()).isEqualTo(1),
                () -> assertThat(concurrentRequester.getFailRequest()).isEqualTo(1),
                () -> assertThat(numberOfSavedMember).isEqualTo(1)
        );
    }

    /*
     * Scenario: 한 명의 사용자가 다수의 레벨로그 작성
     *   when: 동시에 여러 개의 레벨 로그 작성을 요청한다.
     *   then: 한 개의 레벨로그만 작성에 성공한다.
     */
    @Test
    @DisplayName("레벨로그 동시 작성")
    void concurrentlyCreateLevellog() throws InterruptedException {
        // given
        PEPPER.save();
        EVE.save();
        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final int threadPoolSize = 3;
        final ConcurrentRequester concurrentRequester = new ConcurrentRequester(threadPoolSize);

        // when
        for (int i = 0; i < threadPoolSize; i++) {
            concurrentRequester.execute(() -> saveLevellog("Spring과 React를 학습했습니다.", teamId, PEPPER));
        }
        concurrentRequester.await();

        // then
        final long numberOfLevellogs = getResponse("/api/teams/" + teamId, TeamDetailResponse.class)
                .getParticipants()
                .stream()
                .filter(it -> it.getLevellogId() != null)
                .count();

        assertAll(
                () -> assertThat(concurrentRequester.getSuccessRequest()).isEqualTo(1),
                () -> assertThat(concurrentRequester.getFailRequest()).isEqualTo(2),
                () -> assertThat(numberOfLevellogs).isEqualTo(1)
        );
    }

    /*
     * Scenario: 한 명의 사용자가 같은 대상에 대해 다수의 피드백을 작성
     *   when: 동시에 여러 개의 피드백 작성을 요청한다.
     *   then: 한 개의 피드백만 작성에 성공한다.
     */
    @Test
    @DisplayName("피드백 동시 작성")
    void concurrentlyCreateFeedback() throws InterruptedException {
        // given
        PEPPER.save();
        EVE.save();
        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("동시성을 학습했습니다.", teamId, PEPPER).getLevellogId();
        timeStandard.setInProgress();

        final int threadPoolSize = 2;
        final ConcurrentRequester concurrentRequester = new ConcurrentRequester(threadPoolSize);

        // when
        for (int i = 0; i < threadPoolSize; i++) {
            concurrentRequester.execute(() -> saveFeedback("이브가 페퍼의 레벨로그에 작성한 피드백", levellogId, EVE));
        }
        concurrentRequester.await();

        // then
        final long numberOfPepperFeedbacks = getResponse("/api/levellogs/" + levellogId + "/feedbacks",
                FeedbackResponses.class)
                .getFeedbacks()
                .stream()
                .filter(it -> it.getFrom().getId().equals(EVE.getId()) && it.getTo().getId().equals(PEPPER.getId()))
                .count();

        assertAll(
                () -> assertThat(concurrentRequester.getSuccessRequest()).isEqualTo(1),
                () -> assertThat(concurrentRequester.getFailRequest()).isEqualTo(1),
                () -> assertThat(numberOfPepperFeedbacks).isEqualTo(1)
        );
    }

    /*
     * Scenario: 한 명의 사용자가 같은 인터뷰 질문에 대해 동시에 좋아요를 클릭
     *   when: 동시에 여러 번 좋아요를 누른다.
     *   then: 한 개의 좋아요만 성공한다.
     */
    @Test
    @DisplayName("인터뷰 질문 좋아요 동시 요청")
    void concurrentlyCreateInterviewQuestionLike() throws InterruptedException {
        // given
        PEPPER.save();
        EVE.save();
        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, EVE).getTeamId();
        final String levellogId = saveLevellog("동시성을 학습했습니다.", teamId, PEPPER).getLevellogId();
        timeStandard.setInProgress();
        final String interviewQuestionId = saveInterviewQuestion("concurrency는 어떻게 해결할 수 있나요?", levellogId, EVE)
                .getInterviewQuestionId();

        final int threadPoolSize = 2;
        final ConcurrentRequester concurrentRequester = new ConcurrentRequester(threadPoolSize);

        // when
        for (int i = 0; i < threadPoolSize; i++) {
            concurrentRequester.execute(() -> requestPressLikeInterviewQuestion(interviewQuestionId, PEPPER));
        }
        concurrentRequester.await();

        final int likeCounts = getResponse("/api/interview-questions?keyword=concurrency",
                InterviewQuestionSearchQueryResults.class)
                .getResults()
                .stream()
                .filter(it -> it.getId().equals(Long.valueOf(interviewQuestionId)))
                .findFirst()
                .map(InterviewQuestionSearchQueryResult::getLikeCount)
                .get();

        assertAll(
                () -> assertThat(concurrentRequester.getSuccessRequest()).isEqualTo(1),
                () -> assertThat(concurrentRequester.getFailRequest()).isEqualTo(1),
                () -> assertThat(likeCounts).isEqualTo(1)
        );
    }

    private <T> T getResponse(final String url, final Class<T> T) {
        return get(url, PEPPER.getToken())
                .getResponse()
                .extract().body().as(T);
    }
}
