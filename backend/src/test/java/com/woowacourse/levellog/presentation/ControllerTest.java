package com.woowacourse.levellog.presentation;

import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.presentation.OAuthController;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.presentation.FeedbackController;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.presentation.LevellogController;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.presentation.MyInfoController;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.presentation.TeamController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest({
        FeedbackController.class,
        LevellogController.class,
        TeamController.class,
        OAuthController.class,
        MyInfoController.class
})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

    @MockBean
    protected LevellogService levellogService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected FeedbackService feedbackService;

    @MockBean
    protected TeamService teamService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected OAuthService oAuthService;

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(provider)
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
