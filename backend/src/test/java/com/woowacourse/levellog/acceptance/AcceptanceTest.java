package com.woowacourse.levellog.acceptance;

import static com.woowacourse.levellog.fixture.RestAssuredTemplate.post;
import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.config.DatabaseCleaner;
import com.woowacourse.levellog.config.FakeTimeStandard;
import com.woowacourse.levellog.config.TestConfig;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamWriteDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
abstract class AcceptanceTest {

    protected RequestSpecification specification;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected FakeTimeStandard timeStandard;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp(final RestDocumentationContextProvider contextProvider) {
        setRestAssuredPort();
        setRestDocsSpec(contextProvider);

        timeStandard.setBeforeStarted();
    }

    @AfterEach
    public void tearDown() {
        databaseCleaner.clean();
    }

    private void setRestAssuredPort() {
        RestAssured.port = port;
    }

    /*
     * 기본 생성 snippet은 http-request.adoc, http-response.adoc입니다.
     * Request host는 https://api.levellog.app입니다.
     * 응답 헤더 중 Transfer-Encoding, Date, Keep-Alive, Connection은 제외됩니다.
     */
    private void setRestDocsSpec(final RestDocumentationContextProvider contextProvider) {
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

    protected RestAssuredResponse login(final String nickname) {
        try {
            final GithubProfileDto response = new GithubProfileDto(
                    String.valueOf(((int) System.nanoTime())), nickname, nickname + ".com");
            final String code = objectMapper.writeValueAsString(response);

            return post("/api/auth/login", new GithubCodeDto(code));
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected RestAssuredResponse requestCreateTeam(final String title, final String token,
                                                    final int interviewerNumber, final List<Long> participantIds) {
        return requestCreateTeam(title, token, interviewerNumber, participantIds, TEAM_START_TIME);
    }

    protected RestAssuredResponse requestCreateTeam(final String title, final String token,
                                                    final int interviewerNumber, final List<Long> participantIds,
                                                    final LocalDateTime startTime) {
        final ParticipantIdsDto participantIdsDto = new ParticipantIdsDto(participantIds);
        final TeamWriteDto request = new TeamWriteDto(title, title + "place", interviewerNumber, startTime,
                participantIdsDto);

        return post("/api/teams", token, request);
    }
}
