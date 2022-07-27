package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.feedback.dto.CreateFeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.fixture.RestAssuredTemplate;
import com.woowacourse.levellog.levellog.dto.LevellogRequest;
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
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rick_id = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, rick_id, roma_id)
                .getTeamId();

        final LevellogRequest levellogRequest = new LevellogRequest("레벨로그");
        final String levellogId = RestAssuredTemplate.post("/api/teams/" + teamId + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();

        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
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
    @DisplayName("피드백 조회")
    void findAllFeedbacks() {
        // given
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rick_id = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, rick_id, roma_id)
                .getTeamId();

        final LevellogRequest levellogRequest = new LevellogRequest("레벨로그");
        final String levellogId = RestAssuredTemplate.post("/api/teams/" + teamId + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();

        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);

        RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/find-all"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.feedback.study", contains("Spring에 대한 학습을 충분히 하였습니다."),
                        "feedbacks.feedback.speak", contains("아이 컨텍이 좋습니다."),
                        "feedbacks.feedback.etc", contains("윙크하지 마세요.")
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
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rick_id = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, rick_id, roma_id).getTeamId();

        final LevellogRequest levellogRequest = new LevellogRequest("레벨로그");
        final String rick_levellogId = RestAssuredTemplate.post("/api/teams/" + teamId + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();

        final FeedbackContentDto givenFeedbackContentDto = new FeedbackContentDto(
                "Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요.");
        final CreateFeedbackDto givenRequest = new CreateFeedbackDto(givenFeedbackContentDto);

        RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(givenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", rick_levellogId)
                .then().log().all();

        final Long updateId = requestFindAllFeedbacks(rick_levellogId)
                .extract()
                .as(FeedbacksDto.class)
                .getFeedbacks()
                .get(0)
                .getId();

        // when
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto(
                "수정된 Study 피드백", "수정된 Speak 피드백", "수정된 Etc 피드백");
        final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/update"))
                .when()
                .put("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", rick_levellogId, updateId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllFeedbacks(rick_levellogId)
                .body("feedbacks.feedback.study", contains("수정된 Study 피드백"),
                        "feedbacks.feedback.speak", contains("수정된 Speak 피드백"),
                        "feedbacks.feedback.etc", contains("수정된 Etc 피드백")
                );
    }

    /*
     * Scenario: 피드백 삭제
     *   given: 피드백이 등록되어 있다.
     *   given: 등록된 피드백을 조회한다.
     *   when: 조회한 피드백을 삭제한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("피드백 삭제")
    void deleteFeedback() {
        // given
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rick_id = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("릭 and 로마", rickToken, rick_id, roma_id)
                .getTeamId();

        final LevellogRequest levellogRequest = new LevellogRequest("레벨로그");
        final String rick_levellogId = RestAssuredTemplate.post("/api/teams/" + teamId + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();

        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final CreateFeedbackDto request = new CreateFeedbackDto(feedbackContentDto);

        RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", rick_levellogId)
                .then().log().all();

        final Long deleteId = requestFindAllFeedbacks(rick_levellogId)
                .extract()
                .as(FeedbacksDto.class)
                .getFeedbacks()
                .get(0)
                .getId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .filter(document("feedback/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/feedbacks/{feedbackId}", rick_levellogId, deleteId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllFeedbacks(rick_levellogId)
                .body("feedbacks.id", not(contains(deleteId)));
    }

    private ValidatableResponse requestFindAllFeedbacks(final String levellogId) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .when()
                .get("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();
    }
}
