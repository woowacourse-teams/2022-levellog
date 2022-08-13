package com.woowacourse.levellog.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.List;
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
                .get("/api/my-info")
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
                .put("/api/my-info")
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
        final RestAssuredResponse hostLoginResponse = login("릭");
        final Long rickId = hostLoginResponse.getMemberId();
        final String rickToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final RestAssuredResponse pepperLoginResponse = login("페퍼");
        final Long pepperId = pepperLoginResponse.getMemberId();
        final String pepperToken = pepperLoginResponse.getToken();

        final String team1Id = requestCreateTeam("릭,로마", rickToken, 1, List.of(romaId)).getTeamId();
        final String team2Id = requestCreateTeam("릭,로마,페퍼", romaToken, 1, List.of(rickId, pepperId)).getTeamId();

        final String levellog1Id = requestCreateLevellog("레벨로그1", team1Id, rickToken).getLevellogId();
        final String levellog2Id = requestCreateLevellog("레벨로그2", team2Id, rickToken).getLevellogId();

        timeStandard.setInProgress();

        requestCreateFeedback("로마 피드백", levellog1Id, romaToken);
        requestCreateFeedback("페퍼 피드백", levellog2Id, pepperToken);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + rickToken)
                .filter(document("myinfo/findAllReceivedFeedbacks"))
                .when()
                .get("/api/my-info/feedbacks")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("feedbacks.from.nickname", containsInAnyOrder("페퍼", "로마"),
                        "feedbacks.feedback.study", containsInAnyOrder("study 페퍼 피드백", "study 로마 피드백"),
                        "feedbacks.feedback.speak", containsInAnyOrder("speak 페퍼 피드백", "speak 로마 피드백"),
                        "feedbacks.feedback.etc", containsInAnyOrder("etc 페퍼 피드백", "etc 로마 피드백"));
    }

    /*
     * Scenario: 내가 작성한 레벨로그 조회하기
     *   given: 2개의 팀에서 각각 레벨로그를 작성했다.
     *   when: 내가 작성한 레벨로그 조회를 요청한다.
     *   then: 내가 작성한 레벨로그 (2개) 와 200 OK 상태코드를 응답받는다.
     */
    @Test
    @DisplayName("내가 작성한 레벨로그 조회")
    void findAllMyLevellogs() {
        // given
        final RestAssuredResponse romaLoginResponse = login("로마");
        final String romaToken = romaLoginResponse.getToken();
        final Long romaId = romaLoginResponse.getMemberId();

        final RestAssuredResponse hostLoginResponse = login("호스트");
        final String hostToken = hostLoginResponse.getToken();

        final String teamId1 = requestCreateTeam("잠실 제이슨조", hostToken, 1, List.of(romaId)).getTeamId();
        final String teamId2 = requestCreateTeam("잠실 브리조", hostToken, 1, List.of(romaId)).getTeamId();

        requestCreateLevellog("레벨로그1", teamId1, romaToken);
        requestCreateLevellog("레벨로그2", teamId2, romaToken);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .filter(document("myinfo/findAllMyLevellogs"))
                .when()
                .get("/api/my-info/levellogs")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("levellogs.content", contains("레벨로그1", "레벨로그2"));
    }

    /*
     * Scenario: 내가 참여한 팀 조회하기
     *   given: 나(로마) 는 2개의 팀에 참여했다.
     *   when: 내가 참여한 팀 조회를 요청한다.
     *   then: 내가 참여한 팀 목록 (2개) 과 200 OK 상태코드를 응답받는다.
     */
    @Test
    @DisplayName("내가 참여한 팀 조회")
    void findAllMyTeams() {
        // given
        final RestAssuredResponse romaLoginResponse = login("로마");
        final String romaToken = romaLoginResponse.getToken();
        final Long romaId = romaLoginResponse.getMemberId();

        final String hostToken = login("호스트").getToken();

        final Long pepperId = login("페퍼").getMemberId();
        final Long alienId = login("알린").getMemberId();

        requestCreateTeam("잠실 제이슨조", hostToken, 1, List.of(romaId, pepperId));
        requestCreateTeam("잠실 브리조", hostToken, 1, List.of(romaId, alienId));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .filter(document("myinfo/findAllMyTeam"))
                .when()
                .get("/api/my-info/teams")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("teams.title", contains("잠실 제이슨조", "잠실 브리조"));
    }
}
