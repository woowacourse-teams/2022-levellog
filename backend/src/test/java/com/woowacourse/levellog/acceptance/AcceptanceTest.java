package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.config.TestConfig;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
=======
import com.woowacourse.levellog.authentication.dto.GithubCodeRequest;
import com.woowacourse.levellog.authentication.dto.GithubProfileResponse;
import com.woowacourse.levellog.authentication.dto.LoginResponse;
import com.woowacourse.levellog.config.TestAuthenticationConfig;
import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackRequest;
import com.woowacourse.levellog.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.team.dto.ParticipantIdsRequest;
import com.woowacourse.levellog.team.dto.TeamRequest;
>>>>>>> main
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
abstract class AcceptanceTest {

    protected RequestSpecification specification;

    @Autowired
    protected ObjectMapper objectMapper;

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
    }

    @Deprecated
    protected RestAssuredResponse requestCreateTeam(final String title, final String token,
                                                    final Long... participantIds) {
        final ParticipantIdsDto participantIdsDto = new ParticipantIdsDto(List.of(participantIds));
        final TeamCreateDto request = new TeamCreateDto(title, title + "place", 1, LocalDateTime.now().plusDays(3),
                participantIdsDto);

        return post("/api/teams", token, request);
    }

    @Deprecated
    protected RestAssuredResponse login(final String nickname) {
        try {
            final GithubProfileDto response = new GithubProfileDto(String.valueOf(
                    ((int) System.currentTimeMillis())), nickname,
                    nickname + ".com");
            final String code = objectMapper.writeValueAsString(response);
            return post("/api/auth/login", new GithubCodeDto(code));
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
<<<<<<< HEAD
=======

    protected String getToken(final ValidatableResponse loginResponse) {
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

    protected ValidatableResponse requestCreateFeedback(final String levellogId, final String content,
                                                        final String from) {
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
>>>>>>> main
}
