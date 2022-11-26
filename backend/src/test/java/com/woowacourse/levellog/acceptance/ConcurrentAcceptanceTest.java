package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.EVE;
import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.feedback.dto.request.FeedbackWriteRequest;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.team.dto.response.TeamDetailResponse;
import io.restassured.RestAssured;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@DisplayName("동시성 관련 기능")
class ConcurrentAcceptanceTest extends AcceptanceTest {

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
        final FeedbackWriteRequest request = new FeedbackWriteRequest("동시성에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.", "자신감 있게 말해보세요.");
        timeStandard.setInProgress();

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                RestAssured.given(specification).log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + EVE.getToken())
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                        .then().log().all();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final FeedbackResponses response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all()
                .extract().body().as(FeedbackResponses.class);

        final long numberOfPepperFeedbacks = response.getFeedbacks()
                .stream()
                .filter(it -> it.getFrom().getId().equals(EVE.getId()) && it.getTo().getId().equals(PEPPER.getId()))
                .count();
        assertThat(numberOfPepperFeedbacks).isEqualTo(1);
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
        final LevellogWriteRequest request = new LevellogWriteRequest("Spring과 React를 학습했습니다.");

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        // when
        for (int i = 0; i < 2; i++) {
            executorService.execute(() -> {
                RestAssured.given(specification).log().all()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                        .body(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/api/teams/{teamId}/levellogs", teamId)
                        .then().log().all();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        final TeamDetailResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + PEPPER.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/teams/{teamId}", teamId)
                .then().log().all()
                .extract().body().as(TeamDetailResponse.class);
        final long numberOfLevellogs = response.getParticipants()
                .stream()
                .filter(it -> it.getLevellogId() != null)
                .count();
        assertThat(numberOfLevellogs).isEqualTo(1);
    }
}
