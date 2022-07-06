package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Feedback;
import com.woowacourse.levellog.domain.FeedbackRepository;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class FeedbackServiceTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("피드백을 저장한다.")
    void save() {
        // given
        final FeedbackContentDto feedbackContentDto = new FeedbackContentDto("Spring에 대한 학습을 충분히 하였습니다.",
                "아이 컨텍이 좋습니다.",
                "윙크하지 마세요.");
        final FeedbackCreateRequest request = new FeedbackCreateRequest("로마", feedbackContentDto);

        // when
        final Long id = feedbackService.save(request);

        // then
        final Optional<Feedback> feedback = feedbackRepository.findById(id);
        assertThat(feedback).isPresent();
    }
}
