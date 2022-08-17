package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.feedback.domain.Feedback;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("FeedbackRepository의")
class FeedbackRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellog 메서드는 입력된 레벨로그에 등록된 모든 피드백을 조회한다.")
    void findAllByLevellog() {
        // given
        final Member eve = saveMember("이브", "깃허브_이브");
        final Member rick = saveMember("릭", "깃허브_릭");
        final Member alien = saveMember("알린", "깃허브_알린");

        final Team team = saveTeam(eve, rick, alien);
        final Levellog levellog = saveLevellog(alien, team);

        final Feedback savedFeedback1 = saveFeedback(eve, alien, levellog);
        final Feedback savedFeedback2 = saveFeedback(rick, alien, levellog);

        // when
        final List<Feedback> feedbacks = feedbackRepository.findAllByLevellog(levellog);

        // then
        assertThat(feedbacks).hasSize(2)
                .contains(savedFeedback1, savedFeedback2);
    }

    @Test
    @DisplayName("findAllByToOrderByUpdatedAtDesc 메서드는 입력된 멤버가 받은 피드백을 수정일 기준 내림차순으로 조회한다.")
    void findAllByToOrderByUpdatedAtDesc() {
        // given
        final Member eve = saveMember("이브", "깃허브_이브");
        final Member rick = saveMember("릭", "깃허브_릭");
        final Member alien = saveMember("알린", "깃허브_알린");

        final Team team = saveTeam(eve, rick, alien);
        final Levellog levellog = saveLevellog(alien, team);

        final Feedback savedFeedback1 = saveFeedback(eve, alien, levellog);
        final Feedback savedFeedback2 = saveFeedback(rick, alien, levellog);

        savedFeedback2.updateFeedback("update", "update", "update");

        // when
        final List<Feedback> feedbacks = feedbackRepository.findAllByToOrderByUpdatedAtDesc(alien);

        // then
        assertThat(feedbacks).hasSize(2)
                .containsExactly(savedFeedback2, savedFeedback1);
    }

    @Test
    @DisplayName("existsByLevellogIdAndFromId 메서드는 입력 받은 레벨로그 Id와 작성자 Id로 작성된 피드백의 존재 여부를 반환한다.")
    void existsByLevellogIdAndFromId() {
        // given
        final Member eve = saveMember("이브", "깃허브_이브");
        final Member rick = saveMember("릭", "깃허브_릭");

        final Team team = saveTeam(eve, rick);
        final Levellog levellog = saveLevellog(rick, team);

        saveFeedback(eve, rick, levellog);

        // when
        final boolean isExist1 = feedbackRepository.existsByLevellogIdAndFromId(levellog.getId(), eve.getId());
        final boolean isExist2 = feedbackRepository.existsByLevellogIdAndFromId(levellog.getId() + 1,
                eve.getId());
        final boolean isExist3 = feedbackRepository.existsByLevellogIdAndFromId(levellog.getId(),
                eve.getId() + 1);

        // then
        assertAll(() -> {
            assertThat(isExist1).isTrue();
            assertThat(isExist2).isFalse();
            assertThat(isExist3).isFalse();
        });
    }
}
