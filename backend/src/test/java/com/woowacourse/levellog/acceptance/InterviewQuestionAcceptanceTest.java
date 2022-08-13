package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("잠실 제이슨조", pepperToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("페퍼의 레벨로그", teamId, pepperToken).getLevellogId();

        final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 사용하는 이유?");

        timeStandard.setInProgress();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/save"))
                .when()
                .post("/api/levellogs/{levellogId}/interview-questions", levellogId)
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
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("롬펲 인터뷰", pepperToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("페퍼의 레벨로그", teamId, pepperToken).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        requestSaveInterviewQuestion(levellogId, romaToken, "Spring을 사용하는 이유?");
        requestSaveInterviewQuestion(levellogId, romaToken, "스프링 빈이란?");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/findAll"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions", levellogId)
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
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("롬펲 인터뷰", pepperToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("페퍼의 레벨로그", teamId, pepperToken).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String interviewQuestionId = requestSaveInterviewQuestion(levellogId, romaToken, "Spring을 사용하는 이유?")
                .getInterviewQuestionId();
        final InterviewQuestionDto request = InterviewQuestionDto.from("수정된 인터뷰 질문");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/update"))
                .when()
                .put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        levellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllInterviewQuestion(levellogId, romaToken)
                .body("interviewQuestions.interviewQuestion", contains("수정된 인터뷰 질문"));
    }

    /*
     * Scenario: 인터뷰 질문 삭제
     *   given: 인터뷰 질문이 등록되어 있다.
     *   given: 등록된 인터뷰 질문을 조회한다.
     *   when: 조회한 인터뷰 질문을 삭제한다.
     *   then: 204 No Content 상태 코드를 응답받는다.
     */
    @Test
    @DisplayName("인터뷰 질문 삭제")
    void deleteById() {
        // given
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final String teamId = requestCreateTeam("롬펲 인터뷰", pepperToken, 1, List.of(romaId)).getTeamId();
        final String levellogId = requestCreateLevellog("페퍼의 레벨로그", teamId, pepperToken).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String interviewQuestionId = requestSaveInterviewQuestion(levellogId, romaToken, "Spring을 사용하는 이유?")
                .getInterviewQuestionId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        levellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllInterviewQuestion(levellogId, romaToken)
                .body("interviewQuestions.id", not(contains(interviewQuestionId)));
    }

    private RestAssuredResponse requestSaveInterviewQuestion(final String levellogId,
                                                             final String fromMemberToken,
                                                             final String interviewQuestion) {
        return post("/api/levellogs/" + levellogId + "/interview-questions", fromMemberToken,
                InterviewQuestionDto.from(interviewQuestion));
    }

    private ValidatableResponse requestFindAllInterviewQuestion(final String levellogId,
                                                                final String fromMemberToken) {
        return get("/api/levellogs/" + levellogId + "/interview-questions", fromMemberToken)
                .getResponse();
    }
}
