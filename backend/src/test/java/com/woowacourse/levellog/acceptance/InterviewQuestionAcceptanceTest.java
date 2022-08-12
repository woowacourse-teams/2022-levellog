package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.get;
import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
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

        final TeamWriteDto teamDto = new TeamWriteDto("롬펲 인터뷰", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(romaId)));
        final String teamId = post("/api/teams", pepperToken, teamDto).getTeamId();

        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken, levellogRequest)
                .getLevellogId();

        final InterviewQuestionDto request = InterviewQuestionDto.from("Spring을 사용하는 이유?");

        timeStandard.setInProgress();

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
     * Scenario: 내가 작성한 인터뷰 질문 목록 조회
     *   given: 인터뷰 질문이 등록되어있다.
     *   when: 등록된 모든 인터뷰 질문을 조회한다.
     *   then: 200 OK 상태 코드와 모든 인터뷰 질문을 응답 받는다.
     */
    @Test
    @DisplayName("내가 작성한 인터뷰 질문 목록 조회")
    void findAllMyInterviewQuestion() {
        // given
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamWriteDto teamDto = new TeamWriteDto("롬펲 인터뷰", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(romaId)));
        final String teamId = post("/api/teams", pepperToken, teamDto).getTeamId();

        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken, levellogRequest)
                .getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        requestSaveInterviewQuestion(pepperLevellogId, romaToken, "Spring을 사용하는 이유?");
        requestSaveInterviewQuestion(pepperLevellogId, romaToken, "스프링 빈이란?");

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/find-all-my-interview-question"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions/my", pepperLevellogId)
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
        final RestAssuredResponse pepper = login("페퍼");
        final RestAssuredResponse roma = login("로마");
        final RestAssuredResponse rick = login("릭");

        final String pepperToken = pepper.getToken();
        final TeamWriteDto teamDto = new TeamWriteDto("롬펲 인터뷰", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(roma.getMemberId(), rick.getMemberId())));
        final String teamId = post("/api/teams", pepperToken, teamDto).getTeamId();

        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken, levellogRequest)
                .getLevellogId();

        timeStandard.setInProgress();

        final String url = "/api/levellogs/" + pepperLevellogId + "/interview-questions";
        final String rickToken = rick.getToken();
        final String romaToken = roma.getToken();

        final String rickContent1 = "트랜잭션 전파 옵션 종류는?";
        final String rickContent2 = "프로세스와 스레드의 차이는?";

        final String romaContent1 = "AOP에 대해 설명해주세요.";
        final String romaContent2 = "전략 패턴이 무엇인가요?";
        final String romaContent3 = "프레임워크와 라이브러리의 차이는?";

        post(url, rickToken, InterviewQuestionDto.from(rickContent1));
        post(url, romaToken, InterviewQuestionDto.from(romaContent1));
        post(url, romaToken, InterviewQuestionDto.from(romaContent2));
        post(url, rickToken, InterviewQuestionDto.from(rickContent2));
        post(url, romaToken, InterviewQuestionDto.from(romaContent3));

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/find-all-by-levellog"))
                .when()
                .get("/api/levellogs/{levellogId}/interview-questions", pepperLevellogId)
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
        final RestAssuredResponse hostLoginResponse = login("페퍼");
        hostLoginResponse.getMemberId();
        final String pepperToken = hostLoginResponse.getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamWriteDto teamDto = new TeamWriteDto("롬펲 인터뷰", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(romaId)));
        final String teamId = post("/api/teams", pepperToken, teamDto).getTeamId();

        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String interviewQuestionId = requestSaveInterviewQuestion(pepperLevellogId, romaToken, "Spring을 사용하는 이유?")
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
                        pepperLevellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllMyInterviewQuestion(pepperLevellogId, romaToken)
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
        final String pepperToken = login("페퍼").getToken();

        final RestAssuredResponse romaLoginResponse = login("로마");
        final Long romaId = romaLoginResponse.getMemberId();
        final String romaToken = romaLoginResponse.getToken();

        final TeamWriteDto teamDto = new TeamWriteDto("롬펲 인터뷰", "트랙룸", 1, TEAM_START_TIME,
                new ParticipantIdsDto(List.of(romaId)));
        final String teamId = post("/api/teams", pepperToken, teamDto).getTeamId();

        final LevellogWriteDto levellogRequest = LevellogWriteDto.from("페퍼의 레벨로그");
        final String pepperLevellogId = post("/api/teams/" + teamId + "/levellogs", pepperToken,
                levellogRequest).getLevellogId();

        timeStandard.setInProgress(); // 인터뷰 시작

        final String interviewQuestionId = requestSaveInterviewQuestion(pepperLevellogId, romaToken, "Spring을 사용하는 이유?")
                .getInterviewQuestionId();

        // when
        final ValidatableResponse response = RestAssured.given(specification).log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + romaToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .filter(document("interview-question/delete"))
                .when()
                .delete("/api/levellogs/{levellogId}/interview-questions/{interviewQuestionId}",
                        pepperLevellogId, interviewQuestionId)
                .then().log().all();

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        requestFindAllMyInterviewQuestion(pepperLevellogId, romaToken)
                .body("interviewQuestions.id", not(contains(interviewQuestionId)));
    }

    private RestAssuredResponse requestSaveInterviewQuestion(final String levellogId,
                                                             final String fromMemberToken,
                                                             final String interviewQuestion) {
        return post("/api/levellogs/" + levellogId + "/interview-questions", fromMemberToken,
                InterviewQuestionDto.from(interviewQuestion));
    }

    private ValidatableResponse requestFindAllMyInterviewQuestion(final String levellogId,
                                                                  final String fromMemberToken) {
        return get("/api/levellogs/" + levellogId + "/interview-questions/my", fromMemberToken)
                .getResponse();
    }
}
