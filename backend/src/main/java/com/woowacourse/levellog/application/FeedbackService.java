package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Feedback;
import com.woowacourse.levellog.domain.FeedbackRepository;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public Long save(final FeedbackCreateRequest request) {
        final FeedbackContentDto feedbackContent = request.getFeedback();
        final Feedback feedback = new Feedback(request.getName(), feedbackContent.getStudy(), feedbackContent.getSpeak(),
                feedbackContent.getEtc());

        final Feedback savedFeedback = feedbackRepository.save(feedback);
        return savedFeedback.getId();
    }
}
