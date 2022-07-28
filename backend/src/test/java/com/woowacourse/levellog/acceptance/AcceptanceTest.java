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
import com.woowacourse.levellog.authentication.dto.GithubCodeDto;
import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.config.TestAuthenticationConfig;
import com.woowacourse.levellog.fixture.RestAssuredResponse;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.team.dto.ParticipantIdsRequest;
import com.woowacourse.levellog.team.dto.TeamRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestAuthenticationConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
abstract class AcceptanceTest {

    @Deprecated
    protected static final String MASTER = "토미";

    @Deprecated
    protected String masterToken;

    protected RequestSpecification specification;

    @Autowired
    protected ObjectMapper objectMapper;

    @Deprecated
    private Long masterId;

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

        masterToken = login(MASTER)
                .getToken();
        masterId = login(MASTER)
                .getMemberId();
    }

    @Deprecated
    protected RestAssuredResponse requestCreateTeam(final String title, final String host,
                                                    final String... participants) {
        final List<Long> participantIds = Arrays.stream(participants)
                .map(this::login)
                .map(RestAssuredResponse::getMemberId)
                .collect(Collectors.toList());
        final ParticipantIdsRequest participantIdsRequest = new ParticipantIdsRequest(participantIds);
        final TeamRequest request = new TeamRequest(title, title + "place", LocalDateTime.now(), participantIdsRequest);

        final String token = login(host)
                .getToken();

        return post("/api/teams", token, request);
    }

    @Deprecated
    protected RestAssuredResponse requestCreateTeam(final String title, final String token,
                                                    final Long... participantIds) {
        final ParticipantIdsRequest participantIdsRequest = new ParticipantIdsRequest(List.of(participantIds));
        final TeamRequest request = new TeamRequest(title, title + "place", LocalDateTime.now(), participantIdsRequest);

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

    @Deprecated
    protected RestAssuredResponse requestCreateLevellog(final String teamId, final String content) {
        final LevellogDto request = LevellogDto.from(content);

        return post("/api/teams/" + teamId + "/levellogs", masterToken, request);
    }
}
