package com.woowacourse.levellog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.config.TestConfig;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.NicknameMappingRepository;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.support.TimeStandard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(TestConfig.class)
abstract class ServiceTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected TimeStandard timeStandard;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected OAuthService oAuthService;

    @Autowired
    protected LevellogService levellogService;

    @Autowired
    protected FeedbackService feedbackService;

    @Autowired
    protected InterviewQuestionService interviewQuestionService;

    @Autowired
    protected TeamService teamService;

    @Autowired
    protected PreQuestionService preQuestionService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected NicknameMappingRepository nicknameMappingRepository;

    @Autowired
    protected LevellogRepository levellogRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected InterviewQuestionRepository interviewQuestionRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected PreQuestionRepository preQuestionRepository;
}
