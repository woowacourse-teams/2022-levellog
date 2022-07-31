package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.prequestion.dto.PreQuestionCreateDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("사전 질문 관련 기능")
public class PreQuestionAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 사전 등록 생성하기
     *   given: 1개의 레벨로그와 2명의 사용자가 있다.
     *   when: A가 B 레벨로그의 사전 질문 등록을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/levellogs/{id}/pre-question/{id}를 담아 응답받는다.
     */
    @Test
    @DisplayName("사전 질문 등록하기")
    void create() {
        // given
        final RestAssuredResponse loginResponse1 = login("페퍼");
        final RestAssuredResponse loginResponse2 = login("이브");
        final TeamCreateDto teamRequest = new TeamCreateDto("잠실 제이슨조", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(loginResponse2.getMemberId())));
        final String teamId = post("/api/teams", loginResponse1.getToken(), teamRequest).getTeamId();
        final LevellogDto levellogRequest = LevellogDto.from("페퍼의 레벨로그");
        final String levellogId = post("/api/teams/" + teamId + "/levellogs", loginResponse1.getToken(),
                levellogRequest).getLevellogId();

        final PreQuestionCreateDto request = new PreQuestionCreateDto("이브가 쓴 사전 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse2.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("pre-question/create"))
                .when()
                .post("/api/levellogs/" + levellogId + "/pre-questions")
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, equalTo("/api/levellogs/" + levellogId + "/pre-questions/1"));
    }
}
