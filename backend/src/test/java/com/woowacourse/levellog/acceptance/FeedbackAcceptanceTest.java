package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import com.woowacourse.levellog.dto.FeedbacksResponse;
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
        final FeedbackCreateRequest request = new FeedbackCreateRequest("로마",
                new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.", "아이 컨텍이 좋습니다.", "윙크하지 마세요."));

        // when
        final ValidatableResponse response = requestCreateFeedback(request);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, equalTo("/api/feedbacks"));
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
        final FeedbackCreateRequest request1 = new FeedbackCreateRequest("로마",
                new FeedbackContentDto("로마 스터디", "로마 말하기", "로마 기타"));
        final FeedbackCreateRequest request2 = new FeedbackCreateRequest("알린",
                new FeedbackContentDto("알린 스터디", "알린 말하기", "알린 기타"));

        requestCreateFeedback(request1);
        requestCreateFeedback(request2);

        // when
        final ValidatableResponse response = requestFindAllFeedbacks();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.name", contains("로마", "알린"),
                        "feedbacks.feedback.study", contains("로마 스터디", "알린 스터디"),
                        "feedbacks.feedback.speak", contains("로마 말하기", "알린 말하기"),
                        "feedbacks.feedback.etc", contains("로마 기타", "알린 기타")
                );
    }

    /*
     * Scenario: 피드백 삭제
     *   given: 피드백이 등록되어 있다.
     *   given: 피드백을 모두 조회한다.
     *   when: 특정 피드백을 삭제한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */

    @Test
    @DisplayName("피드백 삭제")
    void deleteFeedback() {
        // given
        final FeedbackCreateRequest request1 = new FeedbackCreateRequest("로마",
                new FeedbackContentDto("로마 스터디", "로마 말하기", "로마 기타"));
        final FeedbackCreateRequest request2 = new FeedbackCreateRequest("알린",
                new FeedbackContentDto("알린 스터디", "알린 말하기", "알린 기타"));

        requestCreateFeedback(request1);
        requestCreateFeedback(request2);

        final Long deleteId = requestFindAllFeedbacks()
                .extract()
                .as(FeedbacksResponse.class)
                .getFeedbacks()
                .get(0)
                .getId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .filter(document("feedback/delete"))
                .when()
                .delete("/api/feedbacks/" + deleteId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());

        requestFindAllFeedbacks().body("feedbacks.id", not(contains(deleteId)));
    }

    private ValidatableResponse requestCreateFeedback(final FeedbackCreateRequest request) {
        return RestAssured.given(specification).log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("feedback/save"))
                .when()
                .post("/api/feedbacks")
                .then().log().all();
    }

    private ValidatableResponse requestFindAllFeedbacks() {
        return RestAssured.given(specification).log().all()
                .filter(document("feedback/find-all"))
                .when()
                .get("/api/feedbacks")
                .then().log().all();
    }
}
