package com.woowacourse.levellog.acceptance;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.woowacourse.levellog.authentication.support.TestAuthenticationConfig;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
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

    protected RequestSpecification specification;

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
}
