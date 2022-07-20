package com.woowacourse.levellog.acceptance;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.authentication.support.TestAuthenticationConfig;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackRequest;
import com.woowacourse.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.dto.ParticipantIdsRequest;
import com.woowacourse.levellog.dto.TeamRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.ValidatableResponseOptions;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(TestAuthenticationConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
abstract class AcceptanceTest {

    protected static final String MASTER = "토미";

    protected String masterToken;
    protected RequestSpecification specification;
    private Long masterId;
    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    /*
     * 기본 생성 snippet은 http-request.adoc, http-response.adoc입니다.
     * Request host는 https://api.levellog.app입니다.
     * 응답 헤더 중 Transfer-Encoding, Date, Keep-Alive, Connection은 제외됩니다.
     */
    @BeforeEach
    public void setUp(final RestDocumentationContextProvider contextProvider) {
        RestAssured.port = port;
        specification = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(contextProvider)
                                .snippets()
                                .withDefaults(httpRequest(), httpResponse())
                                .and()
                                .operationPreprocessors()
                                .withRequestDefaults(
                                        modifyUris()
                                                .scheme("https")
                                                .host("api.levellog.app")
                                                .removePort(),
                                        prettyPrint()
                                ).withResponseDefaults(
                                        removeHeaders(
                                                "Transfer-Encoding",
                                                "Date",
                                                "Keep-Alive",
                                                "Connection"),
                                        prettyPrint()
                                )
                ).build();

        masterToken = getToken(login(MASTER));
        masterId = getMemberId(login(MASTER));
    }

    protected ValidatableResponse requestCreateTeam(final String title, final String host,
                                                    final String... participants) {
        final List<Long> participantIds = Arrays.stream(participants)
                .map(this::login)
                .map(ValidatableResponseOptions::extract)
                .map(it -> it.as(LoginResponse.class))
                .map(LoginResponse::getId)
                .collect(Collectors.toList());
        final ParticipantIdsRequest participantIdsRequest = new ParticipantIdsRequest(participantIds);
        final TeamRequest request = new TeamRequest(title, title + "place", LocalDateTime.now(), participantIdsRequest);

        final String token = getToken(login(host));

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/teams")
                .then().log().all();
    }

    private ValidatableResponse login(final String nickname) {
        try {
            final GithubProfileResponse response = new GithubProfileResponse(String.valueOf(
                    ((int) System.currentTimeMillis())), nickname,
                    nickname + ".com");
            final String code = objectMapper.writeValueAsString(response);
            return RestAssured.given().log().all()
                    .body(new GithubCodeRequest(code))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/api/auth/login")
                    .then().log().all();
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getToken(final ValidatableResponse loginResponse) {
        return loginResponse.extract()
                .as(LoginResponse.class)
                .getAccessToken();
    }

    protected Long getMemberId(final ValidatableResponse loginResponse) {
        return loginResponse.extract()
                .as(LoginResponse.class)
                .getId();
    }

    protected String getTeamId(final ValidatableResponse teamResponse) {
        return teamResponse
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/api/teams/")[1];
    }

    protected ValidatableResponse requestCreateLevellog(final String content, final String title, final String host,
                                                        final String... participants) {
        final ValidatableResponse teamResponse = requestCreateTeam(title, host, participants);
        final String teamId = getTeamId(teamResponse);

        return requestCreateLevellog(teamId, content);
    }

    protected ValidatableResponse requestCreateLevellog(final String teamId, final String content) {
        final LevellogRequest request = new LevellogRequest(content);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + masterToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/teams/{teamId}/levellogs", teamId)
                .then().log().all();
    }

    protected String getLevellogId(final ValidatableResponse levellogResponse) {
        return levellogResponse
                .extract()
                .header(HttpHeaders.LOCATION)
                .split("/levellogs/")[1];
    }

    protected ValidatableResponse requestCreateFeedback(final String levellogId, final String content, final String from) {
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("study - " + content,
                "speak - " + content,
                "etc - " + content);
        final FeedbackRequest request = new FeedbackRequest(feedbackContentDto);

        final ValidatableResponse loginResponse = login(from);
        final String token = getToken(loginResponse);

        return RestAssured.given().log().all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/levellogs/{levellogId}/feedbacks", levellogId)
                .then().log().all();
    }
}
