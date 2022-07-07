package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Feedback;
import com.woowacourse.levellog.domain.FeedbackRepository;
import com.woowacourse.levellog.dto.FeedbackContentDto;
import com.woowacourse.levellog.dto.FeedbackCreateRequest;
import com.woowacourse.levellog.dto.FeedbackResponse;
import com.woowacourse.levellog.dto.FeedbacksResponse;
import java.util.List;
import java.util.stream.Collectors;
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
        final Feedback feedback = new Feedback(request.getName(), feedbackContent.getStudy(),
                feedbackContent.getSpeak(), feedbackContent.getEtc());

        final Feedback savedFeedback = feedbackRepository.save(feedback);
        return savedFeedback.getId();
    }

    public FeedbacksResponse findAll() {
        final List<FeedbackResponse> responses = feedbackRepository.findAll().stream()
                .map(it -> new FeedbackResponse(it.getId(), it.getName(),
                        new FeedbackContentDto(it.getStudy(), it.getSpeak(), it.getEtc())))
                .collect(Collectors.toList());

        return new FeedbacksResponse(responses);
    }

    public void deleteById(final Long id) {
        feedbackRepository.deleteById(id);
    }
}
