package com.woowacourse.levellog.presentation;

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
