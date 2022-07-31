package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.interview_question.dto.InterviewQuestionDto;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
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

@DisplayName("인터뷰 질문 관련 기능")
class InterviewQuestionAcceptanceTest extends AcceptanceTest {

    /*
     * Scenario: 인터뷰 질문 작성
     *   when: 인터뷰 질문 작성을 요청한다.
     *   then: 201 Created 상태 코드와 Location 헤더에 /api/levellogs/{levellogId}/interview-question/{interviewQuestionId}를 담아 응답받는다.
     */
    @Test
    @DisplayName("인터뷰 질문 작성")
    void save() {
        // given
        final RestAssuredResponse hostLoginResponse = login("페퍼");
        final Long pepperId = hostLoginResponse.getMemberId();
        final String pepperToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamCreateDto teamCreateDto = new TeamCreateDto("롬펲 인터뷰", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(pepperId, romaId)));
        final String teamId = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        final LevellogDto levellogRequest = LevellogDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 사용하는 이유?");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/save"))
                .when()
                .post("/api/levellogs/{levellogId}/interview-questions", pepperLevellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(HttpHeaders.LOCATION, notNullValue());
    }

    /*
     * Scenario: 인터뷰 질문 목록 조회
     *   given: 인터뷰 질문이 등록되어있다.
     *   when: 등록된 모든 인터뷰 질문을 조회한다.
     *   then: 200 OK 상태 코드와 모든 인터뷰 질문을 응답 받는다.
     */
    @Test
    @DisplayName("인터뷰 질문 목록 조회")
    void findAll() {
        // given
        final RestAssuredResponse hostLoginResponse = login("페퍼");
        final Long pepperId = hostLoginResponse.getMemberId();
        final String pepperToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamCreateDto teamCreateDto = new TeamCreateDto("롬펲 인터뷰", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(pepperId, romaId)));
        final String teamId = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        final LevellogDto levellogRequest = LevellogDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        requestSaveInterviewQuestion(pepperLevellogId, romaToken, "Spring을 사용하는 이유?");
        requestSaveInterviewQuestion(pepperLevellogId, romaToken, "스프링 빈이란?");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/findAll"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions", pepperLevellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("interviewQuestions.interviewQuestion", contains("Spring을 사용하는 이유?", "스프링 빈이란?"));
    }

    /*
     * Scenario: 인터뷰 질문 수정
     *   given: 인터뷰 질문이 등록되어 있다.
     *   given: 등록된 인터뷰 질문을 조회한다.
     *   when: 조회한 인터뷰 질문을 수정한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("인터뷰 질문 수정")
    void update() {
        // given
        final RestAssuredResponse hostLoginResponse = login("페퍼");
        final Long pepperId = hostLoginResponse.getMemberId();
        final String pepperToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamCreateDto teamCreateDto = new TeamCreateDto("롬펲 인터뷰", "트랙룸", LocalDateTime.now().plusDays(3),
                new ParticipantIdsDto(List.of(pepperId, romaId)));
        final String teamId = post("/api/teams", pepperToken, teamCreateDto).getTeamId();

        final LevellogDto levellogRequest = LevellogDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        final String interviewQuestionId = requestSaveInterviewQuestion(pepperLevellogId, romaToken,
                "Spring을 사용하는 이유?").getInterviewQuestionId();
        System.out.println("interviewQuestionId = " + interviewQuestionId);
        final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/update"))
                .when()
                .put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        pepperLevellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllInterviewQuestion(pepperLevellogId, romaToken)
                .body("interviewQuestions.interviewQuestion", contains("수정된 인터뷰 질문"));
    }

    private RestAssuredResponse requestSaveInterviewQuestion(final String levellogId,
                                                             final String fromMemberToken,
                                                             final String interviewQuestion) {
        return post("/api/levellogs/" + levellogId + "/interview-questions", fromMemberToken,
                InterviewQuestionDto.from(interviewQuestion));
    }

    private ValidatableResponse requestFindAllInterviewQuestion(final String levellogId,
                                                                final String fromMemberToken) {
        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + fromMemberToken)
                .when()
                .get("/api/levellogs/" + levellogId + "/interview-questions")
                .then().log().all();
    }
}
