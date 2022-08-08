package com.woowacourse.levellog.domain;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
abstract class RepositoryTest {

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Autowired
    protected InterviewQuestionRepository interviewQuestionRepository;

    @Autowired
    protected LevellogRepository levellogRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ParticipantRepository participantRepository;

    @Autowired
    protected PreQuestionRepository preQuestionRepository;

    @Autowired
    protected TeamRepository teamRepository;

    protected Member getMember(final String nickname) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".org");
        return memberRepository.save(member);
    }

    protected Team getTeam(final Member host, final Member... members) {
        return getTeam(3, host, members);
    }

    protected Team getTeam(final long days, final Member host, final Member... members) {
        final Team team = teamRepository.save(
                new Team("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(days), "jamsil.img", 1));

        participantRepository.save(new Participant(team, host, true));

        final List<Participant> participants = Arrays.stream(members)
                .map(it -> new Participant(team, it, false))
                .collect(Collectors.toList());
        participantRepository.saveAll(participants);

        return team;
    }

    protected Levellog getLevellog(final Member author, final Team team) {
        final Levellog levellog = Levellog.of(author, team, "levellog content");
        return levellogRepository.save(levellog);
    }

    protected InterviewQuestion getInterviewQuestion(final String content, final Levellog levellog,
                                                     final Member author) {
        final InterviewQuestionDto request = InterviewQuestionDto.from(content);
        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(author, levellog);
        return interviewQuestionRepository.save(interviewQuestion);
    }

    protected Feedback getFeedback(final Member from, final Member to, final Levellog levellog) {
        final Feedback feedback = new Feedback(from, to, levellog, "study from " + from.getNickname(),
                "speak from " + from.getNickname(), "etc from " + from.getNickname());
        return feedbackRepository.save(feedback);
    }
}
