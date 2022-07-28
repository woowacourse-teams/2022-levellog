package com.woowacourse.levellog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.config.TestAuthenticationConfig;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(TestAuthenticationConfig.class)
abstract class ServiceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected OAuthService oAuthService;

    @Autowired
    protected LevellogService levellogService;

    @Autowired
    protected FeedbackService feedbackService;

    @Autowired
    protected TeamService teamService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected LevellogRepository levellogRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected ParticipantRepository participantRepository;
}
