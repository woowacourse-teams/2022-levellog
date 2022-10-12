package com.woowacourse.levellog.application;

import static com.woowacourse.levellog.fixture.TimeFixture.TEAM_START_TIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.levellog.admin.application.AdminService;
import com.woowacourse.levellog.authentication.application.OAuthService;
import com.woowacourse.levellog.authentication.support.JwtTokenProvider;
import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.config.FakeTimeStandard;
import com.woowacourse.levellog.config.TestConfig;
import com.woowacourse.levellog.feedback.application.FeedbackService;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.interviewquestion.application.InterviewQuestionService;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikesRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
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
import com.woowacourse.levellog.team.application.TeamQueryService;
import com.woowacourse.levellog.team.application.TeamService;
import com.woowacourse.levellog.team.domain.ParticipantsFactory;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
    protected TeamQueryService teamQueryService;

    @Autowired
    protected PreQuestionService preQuestionService;

    @Autowired
    protected AdminService adminService;

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
    protected InterviewQuestionLikesRepository interviewQuestionLikesRepository;

    @Autowired
    protected PreQuestionRepository preQuestionRepository;

    @BeforeEach
    void setUp() {
        timeStandard.setBeforeStarted();
    }

    protected LoginStatus getLoginStatus(final Member member) {
        return LoginStatus.fromLogin(member.getId());
    }

    protected Member saveMember(final String nickname) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".org");
        return memberRepository.save(member);
    }

    protected Team saveTeam(final Member host, final Member... members) {
        return saveTeam(TEAM_START_TIME, host, members);
    }

    protected Team saveTeam(final LocalDateTime startAt, final Member host, final Member... members) {
        return saveTeam(startAt, 1, host, members);
    }

    protected Team saveTeam(final int interviewerNumber, final Member host, final Member... members) {
        return saveTeam(TEAM_START_TIME, interviewerNumber, host, members);
    }

    protected Team saveTeam(final int interviewerNumber, final Member host, final List<Member> watchers,
                            final Member... members) {
        return saveTeam(TEAM_START_TIME, interviewerNumber, host, watchers, members);
    }

    protected Team saveTeam(final LocalDateTime startAt, final int interviewerNumber, final Member host,
                            final Member... members) {
        return saveTeam(startAt, interviewerNumber, host, Collections.emptyList(), members);
    }

    protected Team saveTeam(final LocalDateTime startAt, final int interviewerNumber, final Member host,
                            final List<Member> watchers, final Member... members) {
        final TeamDetail teamDetail = new TeamDetail("잠실 네오조", "트랙룸", startAt, "jamsil.img", interviewerNumber);

        final List<Long> participantsIds = Arrays.stream(members)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        participantsIds.add(host.getId());

        final List<Long> watcherIds = watchers.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());

        return teamRepository.save(new Team(teamDetail, host.getId(), participantsIds, watcherIds));
    }

    protected Levellog saveLevellog(final Member author, final Team team) {
        return saveLevellog(author, team, "levellog content");
    }

    protected Levellog saveLevellog(final Member author, final Team team, final String content) {
        final Levellog levellog = new Levellog(author.getId(), team, content);
        return levellogRepository.save(levellog);
    }

    protected InterviewQuestion saveInterviewQuestion(final String content, final Levellog levellog,
                                                      final Member author) {
        final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest(content);
        final InterviewQuestion interviewQuestion = request.toEntity(author.getId(), levellog);
        return interviewQuestionRepository.save(interviewQuestion);
    }

    protected Feedback saveFeedback(final Member from, final Member to, final Levellog levellog) {
        final Feedback feedback = new Feedback(from.getId(), levellog, "study from " + from.getNickname(),
                "speak from " + from.getNickname(), "etc from " + from.getNickname());
        return feedbackRepository.save(feedback);
    }

    protected PreQuestion savePreQuestion(final Levellog levellog, final Member author) {
        return savePreQuestion(levellog, author, author.getNickname() + "이 " + levellog.getId() + "에 작성한 사전질문");
    }

    protected PreQuestion savePreQuestion(final Levellog levellog, final Member author, final String content) {
        final PreQuestion preQuestion = new PreQuestion(levellog, author.getId(), content);
        return preQuestionRepository.save(preQuestion);
    }
}
