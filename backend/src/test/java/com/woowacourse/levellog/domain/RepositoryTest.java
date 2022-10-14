package com.woowacourse.levellog.domain;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackQueryRepository;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.fixture.TimeFixture;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikesRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionQueryRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogQueryRepository;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.domain.NicknameMappingRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionQueryRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.ParticipantsFactory;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamDetail;
import com.woowacourse.levellog.team.domain.TeamQueryRepository;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({
        JpaConfig.class,
        TeamQueryRepository.class,
        FeedbackQueryRepository.class,
        InterviewQuestionQueryRepository.class,
        PreQuestionQueryRepository.class,
        LevellogQueryRepository.class
})
abstract class RepositoryTest {

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected FeedbackQueryRepository feedbackQueryRepository;

    @Autowired
    protected InterviewQuestionRepository interviewQuestionRepository;

    @Autowired
    protected InterviewQuestionLikesRepository interviewQuestionLikesRepository;

    @Autowired
    protected InterviewQuestionQueryRepository interviewQuestionQueryRepository;

    @Autowired
    protected LevellogRepository levellogRepository;

    @Autowired
    protected LevellogQueryRepository levellogQueryRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PreQuestionRepository preQuestionRepository;

    @Autowired
    protected PreQuestionQueryRepository preQuestionQueryRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected TeamQueryRepository teamQueryRepository;

    @Autowired
    protected NicknameMappingRepository nicknameMappingRepository;

    protected LoginStatus getLoginStatus(final Member member) {
        return LoginStatus.fromLogin(member.getId());
    }

    protected Member saveMember(final String nickname) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".org");
        return memberRepository.save(member);
    }

    protected Team saveTeam(final Member host, final Member... members) {
        return saveTeam(host, Collections.emptyList(), members);
    }

    protected Team saveTeam(final Member host, final List<Member> watchers, final Member... members) {
        final TeamDetail teamDetail = new TeamDetail("잠실 네오조", "트랙룸", TimeFixture.TEAM_START_TIME, "jamsil.img", 1);

        final List<Long> participantsIds = new ArrayList<>(List.of(host.getId()));
        final List<Long> participantsIdsWithoutHost = Arrays.stream(members)
                .map(BaseEntity::getId)
                .collect(Collectors.toList());
        participantsIds.addAll(participantsIdsWithoutHost);

        final List<Long> watcherIds = watchers.stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toList());

        return teamRepository.save(new Team(teamDetail, host.getId(), participantsIds, watcherIds));
    }

    protected Levellog saveLevellog(final Member author, final Team team) {
        final Levellog levellog = new Levellog(author.getId(), team, "levellog content");
        return levellogRepository.save(levellog);
    }

    protected InterviewQuestion saveInterviewQuestion(final String content, final Levellog levellog,
                                                      final Member author) {
        final InterviewQuestionWriteRequest request = new InterviewQuestionWriteRequest(content);
        final InterviewQuestion interviewQuestion = request.toEntity(author.getId(), levellog);
        return interviewQuestionRepository.save(interviewQuestion);
    }

    protected InterviewQuestionLikes pressLikeInterviewQuestion(final InterviewQuestion interviewQuestion,
                                                                final Member liker) {
        final InterviewQuestionLikes interviewQuestionLikes = new InterviewQuestionLikes(interviewQuestion.getId(),
                liker.getId());
        return interviewQuestionLikesRepository.save(interviewQuestionLikes);
    }

    protected Feedback saveFeedback(final Member from, final Member to, final Levellog levellog) {
        final Feedback feedback = new Feedback(from.getId(), levellog, "study from " + from.getNickname(),
                "speak from " + from.getNickname(), "etc from " + from.getNickname());
        return feedbackRepository.save(feedback);
    }

    protected PreQuestion savePreQuestion(final Levellog levellog, final Member author) {
        final PreQuestion preQuestion = new PreQuestion(levellog, author.getId(),
                author.getNickname() + "이 " + levellog.getId() + "에 작성한 사전질문");
        return preQuestionRepository.save(preQuestion);
    }
}
