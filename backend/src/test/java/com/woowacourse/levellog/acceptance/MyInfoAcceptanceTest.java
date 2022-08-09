package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackWriteDto;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
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
        final String team1Id = requestCreateTeam("릭,로마", rickToken, roma_id).getTeamId();
        final String team2Id = requestCreateTeam("릭,로마,페퍼", romaToken, rick_id, pepper_id).getTeamId();

        // 레벨로그 생성
        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("레벨로그1,2 내용");
        final String levellogId1 = post("/api/teams/" + team1Id + "/levellogs", rickToken,
                levellogRequest)
                .getLevellogId();
        final String levellogId2 = post("/api/teams/" + team2Id + "/levellogs", rickToken,
                levellogRequest)
                .getLevellogId();

        // 피드백 작성
        final FeedbackContentDto feedbackContentDto1 = new FeedbackContentDto("로마 study 리뷰", "로마 speak 리뷰",
                "로마 etc 리뷰");
        final FeedbackContentDto feedbackContentDto2 = new FeedbackContentDto("페퍼 study 리뷰", "페퍼 speak 리뷰",
                "페퍼 etc 리뷰");
        final FeedbackWriteDto request1 = new FeedbackWriteDto(feedbackContentDto1);
        final FeedbackWriteDto request2 = new FeedbackWriteDto(feedbackContentDto2);
        post("/api/levellogs/" + levellogId1 + "/feedbacks", romaToken, request1); // 로마 -> 릭 리뷰
        post("/api/levellogs/" + levellogId2 + "/feedbacks", pepperToken, request2); // 페퍼 -> 릭 리뷰

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
                        "feedbacks.feedback.study", containsInAnyOrder("페퍼 study 리뷰", "로마 study 리뷰"),
                        "feedbacks.feedback.speak", containsInAnyOrder("페퍼 speak 리뷰", "로마 speak 리뷰"),
                        "feedbacks.feedback.etc", containsInAnyOrder("페퍼 etc 리뷰", "로마 etc 리뷰"));
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
        // 멤버 생성
        final RestAssuredResponse romaLoginResponse = login("로마");
        final String romaToken = romaLoginResponse.getToken();
        final Long romaId = romaLoginResponse.getMemberId();

        final RestAssuredResponse hostLoginResponse = login("호스트");
        final String hostToken = hostLoginResponse.getToken();

        // 팀 생성
        final TeamCreateDto teamCreateDto1 = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(romaId)));
        final TeamCreateDto teamCreateDto2 = new TeamCreateDto("잠실 브리조", "톱오브스윙방", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(romaId)));

        final String teamId1 = post("/api/teams", hostToken, teamCreateDto1).getTeamId();
        final String teamId2 = post("/api/teams", hostToken, teamCreateDto2).getTeamId();

        // 레벨로그 생성
        final LevellogWriteDto levellogRequest1 = LevellogWriteDto.from("레벨로그1 내용");
        final LevellogWriteDto levellogRequest2 = LevellogWriteDto.from("레벨로그2 내용");

        post("/api/teams/" + teamId1 + "/levellogs", romaToken, levellogRequest1).getLevellogId();
        post("/api/teams/" + teamId2 + "/levellogs", romaToken, levellogRequest2).getLevellogId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .filter(document("myinfo/findAllMyLevellogs"))
                .when()
                .get("/api/my-info/levellogs")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("levellogs.content", contains("레벨로그1 내용", "레벨로그2 내용"));
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
        // 멤버 생성
        final RestAssuredResponse romaLoginResponse = login("로마");
        final String romaToken = romaLoginResponse.getToken();
        final Long romaId = romaLoginResponse.getMemberId();

        final RestAssuredResponse hostLoginResponse = login("호스트");
        final String hostToken = hostLoginResponse.getToken();

        final Long pepperId = login("페퍼").getMemberId();
        final Long alienId = login("알린").getMemberId();

        // 팀 생성
        final TeamCreateDto teamCreateDto1 = new TeamCreateDto("잠실 제이슨조", "트랙룸", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(romaId, pepperId)));
        final TeamCreateDto teamCreateDto2 = new TeamCreateDto("잠실 브리조", "톱오브스윙방", 1, LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(romaId, alienId)));

        post("/api/teams", hostToken, teamCreateDto1);
        post("/api/teams", hostToken, teamCreateDto2);

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
