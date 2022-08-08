package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("TeamRepository의")
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private InterviewQuestionRepository interviewQuestionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LevellogRepository levellogRepository;

    @Test
    @DisplayName("findByInterviewQuestion 메서드는 매개 변수로 받은 인터뷰 질문을 갖는 팀을 조회한다.")
    void findByInterviewQuestion() {
        // given
        final Member author = memberRepository.save(new Member("알린", 123, "알린.img"));
        final Team team = teamRepository.save(new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img", 1));
        final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

        final Member observer = memberRepository.save(new Member("릭", 1231234, "릭.img"));
        final String content = "로마가 쓴 인터뷰 질문입니다.";
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.save(
                InterviewQuestion.of(observer, levellog, content));

        // when
        final Optional<Team> actual = teamRepository.findByInterviewQuestion(interviewQuestion);

        // then
        assertThat(actual).isNotEmpty();
    }
}
