package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.feedback.dto.FeedbackDto;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FeedbackQueryRepository의")
class FeedbackQueryRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellog 메서드는 입력된 레벨로그에 등록된 모든 피드백을 조회한다.")
    void findAllByLevellog() {
        // given
        final Member eve = saveMember("eve");
        final Member rick = saveMember("rick");
        final Member toMember = saveMember("toMember");

        final Team team = saveTeam(eve, rick, toMember);
        final Levellog levellog = saveLevellog(toMember, team);

        saveFeedback(eve, toMember, levellog);
        saveFeedback(rick, toMember, levellog);

        // when
        final List<FeedbackDto> feedbacks = feedbackQueryRepository.findAllByLevellog(levellog.getId())
                .getFeedbacks();

        // then
        assertThat(feedbacks).hasSize(2)
                .extracting(it -> it.getFrom().getId(), it -> it.getTo().getId())
                .containsExactly(
                        tuple(eve.getId(), toMember.getId()),
                        tuple(rick.getId(), toMember.getId())
                );
    }
}
