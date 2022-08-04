package com.woowacourse.levellog.presentation;

<<<<<<< HEAD
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

=======
>>>>>>> main
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.presentation.OAuthController;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.presentation.FeedbackController;
<<<<<<< HEAD
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.presentation.InterviewQuestionController;
=======
>>>>>>> main
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.presentation.LevellogController;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.presentation.MyInfoController;
<<<<<<< HEAD
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.presentation.PreQuestionController;
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
=======
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.presentation.TeamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
>>>>>>> main

@WebMvcTest({
        FeedbackController.class,
        LevellogController.class,
        TeamController.class,
        OAuthController.class,
<<<<<<< HEAD
        MyInfoController.class,
        PreQuestionController.class,
        InterviewQuestionController.class
})
@ExtendWith(RestDocumentationExtension.class)
=======
        MyInfoController.class
})
>>>>>>> main
public abstract class ControllerTest {

    @MockBean
    protected LevellogService levellogService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected FeedbackService feedbackService;

    @MockBean
<<<<<<< HEAD
    protected InterviewQuestionService interviewQuestionService;

    @MockBean
    protected TeamService teamService;

    @MockBean
    protected PreQuestionService preQuestionService;

    @MockBean
    protected OAuthService oAuthService;
=======
    protected TeamService teamService;

    @MockBean
    private OAuthService oAuthService;
>>>>>>> main

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

<<<<<<< HEAD
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

    protected ResultActions requestGet(final String url, final String token) throws Exception {
        return mockMvc.perform(get(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    protected ResultActions requestPost(final String url, final String token, final Object request)
            throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(post(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    protected ResultActions requestPut(final String url, final String token, final Object request)
            throws Exception {
        final String content = objectMapper.writeValueAsString(request);

        return mockMvc.perform(put(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print());
    }

    protected ResultActions requestDelete(final String url, final String token) throws Exception {
        return mockMvc.perform(delete(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
=======
>>>>>>> main
}
