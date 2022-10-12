package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.MemberFixture.RICK;
import static com.woowacourse.levellog.fixture.MemberFixture.ROMA;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.feedback.dto.request.FeedbackWriteRequest;
import com.woowacourse.levellog.fixture.RestAssuredTemplate;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("피드백 관련 기능")
class FeedbackAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 피드백 작성
     *   when: 피드백 작성을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/feedbacks를 담아 응답받는다.
     */
    @Test
    @DisplayName("피드백 작성")
    void createFeedback() {
        // given
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("릭 and 로마", RICK, 1, RICK, ROMA).getTeamId();
        final String levellogId = saveLevellog("레벨로그", teamId, RICK).getLevellogId();

        timeStandard.setInProgress();

        // when
        final FeedbackWriteRequest request = new FeedbackWriteRequest("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.", "윙크하지 마세요.");

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    /*
     * Scenario: 피드백 전체 조회
     *   given: 피드백이 등록되어있다.
     *   when: 등록된 모든 피드백을 조회한다.
     *   then: 200 OK 상태 코드와 모든 피드백을 응답 받는다.
     */
    @Test
    @DisplayName("피드백 전체 조회")
    void findAllFeedbacks() {
        // given
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("릭 and 로마", RICK, 1, RICK, ROMA).getTeamId();
        final String levellogId = saveLevellog("레벨로그", teamId, RICK).getLevellogId();

        timeStandard.setInProgress();

        saveFeedback("test", levellogId, ROMA);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/find-all"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.from.nickname", contains("로마"),
                        "feedbacks.to.nickname", contains("릭"),
                        "feedbacks.feedback.study", contains("study test"),
                        "feedbacks.feedback.speak", contains("speak test"),
                        "feedbacks.feedback.etc", contains("etc test")
                );
    }

    /*
     * Scenario: 피드백 상세 조회
     *   given: 피드백이 등록되어있다.
     *   when: 등록된 모든 피드백을 조회한다.
     *   then: 200 OK 상태 코드와 모든 피드백을 응답 받는다.
     */
    @Test
    @DisplayName("피드백 상세 조회")
    void findByIdFeedbacks() {
        // given
        RICK.save();
        ROMA.save();
        PEPPER.save();

        final String teamId = saveTeam("릭 and 로마 and 페퍼", RICK, 1, RICK, ROMA, PEPPER).getTeamId();
        final String levellogId = saveLevellog("레벨로그", teamId, RICK).getLevellogId();

        timeStandard.setInProgress();

        final String feedbackId = saveFeedback("로마가 쓴 피드백", levellogId, ROMA).getFeedbackId();
        saveFeedback("페퍼가 쓴 피드백", levellogId, PEPPER);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/find-by-id"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("id", is(Integer.parseInt(feedbackId)),
                        "from.nickname", is("로마"),
                        "to.nickname", is("릭"),
                        "feedback.study", is("study 로마가 쓴 피드백"),
                        "feedback.speak", is("speak 로마가 쓴 피드백"),
                        "feedback.etc", is("etc 로마가 쓴 피드백")
                );
    }

    /*
     * Scenario: 피드백 수정
     *   given: 피드백이 등록되어 있다.
     *   given: 등록된 피드백을 조회한다.
     *   when: 조회한 피드백 내용을 수정한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("피드백 수정")
    void updateFeedback() {
        // given
        RICK.save();
        ROMA.save();

        final String teamId = saveTeam("릭 and 로마", RICK, 1, RICK, ROMA).getTeamId();
        final String levellogId = saveLevellog("레벨로그", teamId, RICK).getLevellogId();

        timeStandard.setInProgress();

        final String feedbackId = saveFeedback("test", levellogId, ROMA).getFeedbackId();

        // when
        final FeedbackWriteRequest request = new FeedbackWriteRequest("수정된 Study 피드백", "수정된 Speak 피드백", "수정된 Etc 피드백");

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/update"))
                .when()
                .put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", levellogId, feedbackId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        RestAssuredTemplate.get("/api/levellogs/" + levellogId + "/feedbacks", ROMA.getToken())
                .getResponse()
                .body("feedbacks.feedback.study", contains("수정된 Study 피드백"),
                        "feedbacks.feedback.speak", contains("수정된 Speak 피드백"),
                        "feedbacks.feedback.etc", contains("수정된 Etc 피드백")
                );
    }
}
