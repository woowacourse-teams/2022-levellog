package com.woowacourse.levellog.domain;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.feedback.domain.FeedbackRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.TeamRepository;
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
}
