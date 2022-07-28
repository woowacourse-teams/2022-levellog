package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackRequest;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.fixture.RestAssuredTemplate;
import com.woowacourse.levellog.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpHeaders;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("MyInfo 관련 기능")
class MyInfoAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 내 정보 조회
     *   given: 로그인이 되어있다.
     *   when: 내 정보를 조회한다.
     *   then: 로그인 된 나의 정보와 200 Ok 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("내 정보 조회")
    void readMyInfo() {
        // given
        final String token = login("로마")
                .getToken();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("myinfo/read"))
                .when()
                .get("/api/myInfo")
                .then().log().all();

        // then
        response.body("id", Matchers.notNullValue(),
                "nickname", equalTo("로마"),
                "profileUrl", equalTo("로마.com"));
    }

    /*
     * Scenario: 내 닉네임 변경하기
     *   given: 로그인이 되어있다.
     *   when: 닉네임을 변경한다.
     *   then: 닉네임이 변경되고 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("내 닉네임 변경")
    void updateMyNickname() {
        // given
        final String token = login("로마").getToken();

        final NicknameUpdateDto nicknameDto = new NicknameUpdateDto("새이름");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(nicknameDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("myinfo/update/nickname"))
                .when()
                .put("/api/myInfo")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    /*
     * Scenario: 내가 받은 피드백 조회하기
     *   given: 두 팀에 속해있고 각 팀에서 하나의 피드백을 받았다.
     *   when: 내가 받은 피드백 조회를 요청한다.
     *   then: 내가 받은 피드백 (2개) 과 200 OK 상태코드를 응답받는다.
     */
    @Test
    @DisplayName("내가 받은 피드백 조회")
    void findAllFeedbackToMe() {
        // given
        // 회원 로그인
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rick_id = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long roma_id = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final RestAssuredResponse pepperLoginResponse = login("페퍼");
        final Long pepper_id = pepperLoginResponse.getMemberId();
        final String pepperToken = pepperLoginResponse.getToken();

        // 팀 생성
        final String team1Id = requestCreateTeam("릭,로마", rickToken, rick_id, roma_id).getTeamId();
        final String team2Id = requestCreateTeam("릭,로마,페퍼", romaToken, rick_id, roma_id, pepper_id).getTeamId();

        // 레벨로그 생성
        final LevellogRequest levellogRequest = new LevellogRequest("레벨로그1,2 내용");
        final String levellogId1 = RestAssuredTemplate.post("/api/teams/" + team1Id + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();
        final String levellogId2 = RestAssuredTemplate.post("/api/teams/" + team2Id + "/levellogs", rickToken,
                        levellogRequest)
                .getLevellogId();

        // 피드백 작성
        final FeedbackContentDto feedbackContentDto1 = new FeedbackContentDto("로마 study 리뷰", "로마 speak 리뷰",
                "로마 etc 리뷰");
        final FeedbackContentDto feedbackContentDto2 = new FeedbackContentDto("페퍼 study 리뷰", "페퍼 speak 리뷰",
                "페퍼 etc 리뷰");
        final FeedbackRequest request1 = new FeedbackRequest(feedbackContentDto1);
        final FeedbackRequest request2 = new FeedbackRequest(feedbackContentDto2);
        RestAssuredTemplate.post("/api/levellogs/" + levellogId1 + "/feedbacks", romaToken, request1); // 로마 -> 릭 리뷰
        RestAssuredTemplate.post("/api/levellogs/" + levellogId2 + "/feedbacks", pepperToken, request2); // 페퍼 -> 릭 리뷰

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + rickToken)
                .filter(document("myinfo/findAllToMe"))
                .when()
                .get("/api/myInfo/feedbacks")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.from.nickname", containsInAnyOrder("페퍼", "로마"),
                        "feedbacks.feedback.study", containsInAnyOrder("페퍼 study 리뷰", "로마 study 리뷰"),
                        "feedbacks.feedback.speak", containsInAnyOrder("페퍼 speak 리뷰", "로마 speak 리뷰"),
                        "feedbacks.feedback.etc", containsInAnyOrder("페퍼 etc 리뷰", "로마 etc 리뷰"));
    }
}
