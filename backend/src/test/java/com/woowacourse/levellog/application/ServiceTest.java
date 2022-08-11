package com.woowacourse.levellog.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.config.FakeTimeStandard;
import com.woowacourse.levellog.config.TestConfig;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.levellog.application.LevellogService;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.domain.NicknameMappingRepository;
import com.woowacourse.levellog.prequestion.application.PreQuestionService;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected FakeTimeStandard timeStandard;

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

    @BeforeEach
    void setUp() {
        timeStandard.setBeforeStarted();
    }

    protected Member saveMember(final String nickname) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".org");
        return memberRepository.save(member);
    }

    protected Team saveTeam(final Member host, final Member... members) {
        final Team team = teamRepository.save(new Team("잠실 네오조", "트랙룸", TimeFixture.TEAM_START_TIME, "jamsil.img", 1));

        participantRepository.save(new Participant(team, host, true));

        final List<Participant> participants = Arrays.stream(members)
                .map(it -> new Participant(team, it, false))
                .collect(Collectors.toList());
        participantRepository.saveAll(participants);

        return team;
    }

    protected Levellog saveLevellog(final Member author, final Team team) {
        final Levellog levellog = Levellog.of(author, team, "levellog content");
        return levellogRepository.save(levellog);
    }

    protected InterviewQuestion saveInterviewQuestion(final String content, final Levellog levellog,
                                                      final Member author) {
        final InterviewQuestionDto request = InterviewQuestionDto.from(content);
        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(author, levellog);
        return interviewQuestionRepository.save(interviewQuestion);
    }

    protected Feedback saveFeedback(final Member from, final Member to, final Levellog levellog) {
        final Feedback feedback = new Feedback(from, to, levellog, "study from " + from.getNickname(),
                "speak from " + from.getNickname(), "etc from " + from.getNickname());
        return feedbackRepository.save(feedback);
    }

    protected PreQuestion savePreQuestion(final Levellog levellog, final Member author) {
        final PreQuestion preQuestion = new PreQuestion(levellog, author,
                author.getNickname() + "이 " + levellog.getId() + "에 작성한 사전질문");
        return preQuestionRepository.save(preQuestion);
    }
}
