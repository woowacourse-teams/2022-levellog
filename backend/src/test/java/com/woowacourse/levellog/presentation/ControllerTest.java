package com.woowacourse.levellog.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.admin.presentation.AdminController;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.presentation.OAuthController;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.presentation.FeedbackController;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.presentation.InterviewQuestionController;
import com.woowacourse.levellog.interviewquestion.presentation.InterviewQuestionSearchController;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.presentation.LevellogController;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.presentation.MyInfoController;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.presentation.PreQuestionController;
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.presentation.TeamController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest({
        FeedbackController.class,
        LevellogController.class,
        TeamController.class,
        OAuthController.class,
        MyInfoController.class,
        PreQuestionController.class,
        InterviewQuestionController.class,
        InterviewQuestionSearchController.class,
        AdminController.class
})
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

    protected static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiaWF0";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LevellogService levellogService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected FeedbackService feedbackService;

    @MockBean
    protected InterviewQuestionService interviewQuestionService;

    @MockBean
    protected TeamService teamService;

    @MockBean
    protected TeamQueryService teamQueryService;

    @MockBean
    protected PreQuestionService preQuestionService;

    @MockBean
    protected OAuthService oAuthService;

    @MockBean
    protected AdminService adminService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        setMockMvcRestDocsSpec(context, provider);
        mockLogin();
    }

    private void setMockMvcRestDocsSpec(final WebApplicationContext context,
                                        final RestDocumentationContextProvider provider) {
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

    private void mockLogin() {
        given(jwtTokenProvider.getPayload(VALID_TOKEN)).willReturn("1");
        given(jwtTokenProvider.validateToken(VALID_TOKEN)).willReturn(true);
    }

    protected ResultActions requestGet(final String url) throws Exception {
        return mockMvc.perform(get(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions requestPost(final String url) throws Exception {
        return requestPost(url, null);
    }

    protected ResultActions requestPost(final String url, final Object request) throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    protected ResultActions requestPut(final String url, final Object request) throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(put(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    protected ResultActions requestDelete(final String url) throws Exception {
        return mockMvc.perform(delete(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + VALID_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
