package com.woowacourse.levellog.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.application.FeedbackService;
import com.woowacourse.levellog.application.LevellogService;
import com.woowacourse.levellog.application.MemberService;
import com.woowacourse.levellog.application.TeamService;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.domain.JwtTokenProvider;
import com.woowacourse.levellog.authentication.presentation.OAuthController;
import com.woowacourse.levellog.presentation.FeedbackController;
import com.woowacourse.levellog.presentation.LevellogController;
import com.woowacourse.levellog.presentation.TeamController;
import com.woowacourse.levellog.presentation.MyInfoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        FeedbackController.class,
        LevellogController.class,
        TeamController.class,
        OAuthController.class,
        MyInfoController.class
})
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
    private OAuthService oAuthService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
