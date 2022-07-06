package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.equalTo;

import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
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
        final ValidatableResponse response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/feedbacks")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, equalTo("/api/feedbacks"));
    }
}
