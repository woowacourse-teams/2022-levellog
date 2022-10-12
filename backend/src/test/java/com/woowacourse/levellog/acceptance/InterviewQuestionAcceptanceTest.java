package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.MemberFixture.PEPPER;
import static com.woowacourse.levellog.fixture.MemberFixture.RICK;
import static com.woowacourse.levellog.fixture.MemberFixture.ROMA;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.MemberFixture;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
        PEPPER.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        timeStandard.setInProgress();

        // when
        final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("Spring을 사용하는 이유?");

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
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
     * Scenario: 내가 작성한 인터뷰 질문 목록 조회
     *   given: 레벨로그와 인터뷰 질문이 등록되어있다.
     *   when: 등록된 레벨로그에 내가 작성한 모든 인터뷰 질문을 조회한다.
     *   then: 200 OK 상태 코드와 내가 작성한 모든 인터뷰 질문을 응답 받는다.
     */
    @Test
    @DisplayName("내가 작성한 인터뷰 질문 목록 조회")
    void findAllMyInterviewQuestion() {
        // given
        PEPPER.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        saveInterviewQuestion("Spring을 사용하는 이유?", levellogId, ROMA);
        saveInterviewQuestion("스프링 빈이란?", levellogId, ROMA);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/find-all-my-interview-question"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions/my", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("interviewQuestions.content", contains("Spring을 사용하는 이유?", "스프링 빈이란?"));
    }

    /*
     * Scenario: 레벨로그에 작성된 인터뷰 질문 목록 조회
     *   given: 페퍼, 로마, 릭이 참가자인 팀이 있다.
     *   given: 페퍼는 레벨로그를 작성한다.
     *   given: 인터뷰를 시작한다.
     *   given: 로마와 릭은 페퍼의 레벨로그에 대한 인터뷰 질문을 작성한다.
     *   when: 페퍼의 레벨로그에 작성된 모든 인터뷰 질문 목록을 조회힌다.
     *   then: 200 OK 상태 코드와 레벨로그에 작성된 모든 인터뷰 질문을 응답 받는다.
     */
    @Test
    @DisplayName("레벨로그에 작성된 인터뷰 질문 목록 조회")
    void findAllByLevellog() {
        // given
        PEPPER.save();
        ROMA.save();
        RICK.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA, RICK).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String rickContent1 = "트랜잭션 전파 옵션 종류는?";
        saveInterviewQuestion(rickContent1, levellogId, RICK);

        final String romaContent1 = "AOP에 대해 설명해주세요.";
        saveInterviewQuestion(romaContent1, levellogId, ROMA);

        final String romaContent2 = "전략 패턴이 무엇인가요?";
        saveInterviewQuestion(romaContent2, levellogId, ROMA);

        final String rickContent2 = "프로세스와 스레드의 차이는?";
        saveInterviewQuestion(rickContent2, levellogId, RICK);

        final String romaContent3 = "프레임워크와 라이브러리의 차이는?";
        saveInterviewQuestion(romaContent3, levellogId, ROMA);

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/find-all-by-levellog"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions", levellogId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.OK.value())
                .body("interviewQuestions", hasSize(2),

                        "interviewQuestions[0].author.nickname", equalTo("릭"),
                        "interviewQuestions[0].contents.content", contains(rickContent1, rickContent2),

                        "interviewQuestions[1].author.nickname", equalTo("로마"),
                        "interviewQuestions[1].contents.content", contains(romaContent1, romaContent2, romaContent3)
                );
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
        PEPPER.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String interviewQuestionId = saveInterviewQuestion("Spring을 사용하는 이유?", levellogId, ROMA)
                .getInterviewQuestionId();

        // when
        final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest("수정된 인터뷰 질문");

        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/update"))
                .when()
                .put("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        levellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllMyInterviewQuestion(levellogId, ROMA)
                .body("interviewQuestions.content", contains("수정된 인터뷰 질문"));
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
        PEPPER.save();
        ROMA.save();

        final String teamId = saveTeam("잠실 제이슨조", PEPPER, 1, PEPPER, ROMA).getTeamId();
        final String levellogId = saveLevellog("페퍼의 레벨로그", teamId, PEPPER).getLevellogId();

        timeStandard.setInProgress();

        final String interviewQuestionId = saveInterviewQuestion("Spring을 사용하는 이유?", levellogId, ROMA)
                .getInterviewQuestionId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + ROMA.getToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        levellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllMyInterviewQuestion(levellogId, ROMA)
                .body("interviewQuestions.id", not(contains(interviewQuestionId)));
    }

    private ValidatableResponse requestFindAllMyInterviewQuestion(final String levellogId, final MemberFixture from) {
        return get("/api/levellogs/" + levellogId + "/interview-questions/my", from.getToken())
                .getResponse();
    }
}
