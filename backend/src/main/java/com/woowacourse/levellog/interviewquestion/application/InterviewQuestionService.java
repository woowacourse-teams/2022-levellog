package com.woowacourse.levellog.interviewquestion.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestion;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikes;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionLikesRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionQueryRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionRepository;
import com.woowacourse.levellog.interviewquestion.domain.InterviewQuestionSort;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResults;
import com.woowacourse.levellog.interviewquestion.dto.request.InterviewQuestionWriteRequest;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponse;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponses;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionResponse;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionResponses;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikeNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikesAlreadyExistException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewQuestionQueryRepository interviewQuestionQueryRepository;
    private final InterviewQuestionLikesRepository interviewQuestionLikesRepository;
    private final LevellogRepository levellogRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final InterviewQuestionWriteRequest request, final Long levellogId,
                     @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfInterviewQuestion(loginStatus.getMemberId());
        team.validateIsParticipants(loginStatus.getMemberId());
        team.validateInProgress(timeStandard.now());

        final InterviewQuestion interviewQuestion = request.toEntity(loginStatus.getMemberId(), levellog);
        return interviewQuestionRepository.save(interviewQuestion)
                .getId();
    }

    public InterviewQuestionResponses findAllByLevellog(final Long levellogId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final List<InterviewQuestionResponse> interviewQuestions = interviewQuestionQueryRepository.findAllByLevellog(
                        levellog)
                .stream()
                .collect(Collectors.groupingBy(
                                InterviewQuestionQueryResult::getAuthor,
                                LinkedHashMap::new,
                                Collectors.mapping(InterviewQuestionQueryResult::getContent, Collectors.toList())
                        )
                ).entrySet()
                .stream()
                .map(it -> new InterviewQuestionResponse(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        return new InterviewQuestionResponses(interviewQuestions);
    }

    public InterviewQuestionContentResponses findAllByLevellogAndAuthor(final Long levellogId,
                                                                        @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthorId(
                levellog, loginStatus.getMemberId());

        return new InterviewQuestionContentResponses(interviewQuestions.stream()
                .map(it -> new InterviewQuestionContentResponse(it.getId(), it.getContent()))
                .collect(Collectors.toList()));
    }

    public InterviewQuestionSearchQueryResults searchByKeyword(final String keyword,
                                                               @Verified final LoginStatus loginStatus,
                                                               final Long size, final Long page, final String sort) {
        final InterviewQuestionSort sortCondition = InterviewQuestionSort.valueOf(sort.toUpperCase());
        final List<InterviewQuestionSearchQueryResult> results = interviewQuestionQueryRepository.searchByKeyword(
                keyword,
                loginStatus, size, page, sortCondition);

        return new InterviewQuestionSearchQueryResults(results, page);
    }

    @Transactional
    public void update(final InterviewQuestionWriteRequest request, final Long interviewQuestionId,
                       @Verified final LoginStatus loginStatus) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);

        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestion.updateContent(request.getContent(), loginStatus.getMemberId());
    }

    @Transactional
    public void deleteById(final Long interviewQuestionId, @Verified final LoginStatus loginStatus) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);

        interviewQuestion.validateMemberIsAuthor(loginStatus.getMemberId());
        interviewQuestion.getLevellog()
                .getTeam()
                .validateInProgress(timeStandard.now());

        interviewQuestionRepository.delete(interviewQuestion);
    }

    @Transactional
    public void pressLike(final Long interviewQuestionId, @Verified final LoginStatus loginStatus) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        validateAlreadyExist(interviewQuestionId, loginStatus.getMemberId());

        interviewQuestionLikesRepository.save(
                new InterviewQuestionLikes(interviewQuestion.getId(), loginStatus.getMemberId()));
        interviewQuestion.upLike();
    }

    @Transactional
    public void cancelLike(final Long interviewQuestionId, @Verified final LoginStatus loginStatus) {
        final InterviewQuestion interviewQuestion = interviewQuestionRepository.getInterviewQuestion(
                interviewQuestionId);
        final InterviewQuestionLikes interviewQuestionLikes = getInterviewQuestionLikes(interviewQuestion,
                loginStatus.getMemberId());

        interviewQuestionLikesRepository.deleteById(interviewQuestionLikes.getId());
        interviewQuestion.downLike();
    }

    private InterviewQuestionLikes getInterviewQuestionLikes(final InterviewQuestion interviewQuestion,
                                                             final Long memberId) {
        return interviewQuestionLikesRepository.findByInterviewQuestionIdAndLikerId(interviewQuestion.getId(),
                        memberId)
                .orElseThrow(() -> new InterviewQuestionLikeNotFoundException(
                        DebugMessage.init()
                                .append("interviewQuestionId", interviewQuestion.getId())
                                .append("likerId", memberId)
                ));
    }

    private void validateAlreadyExist(final Long interviewQuestionId, final Long memberId) {
        if (interviewQuestionLikesRepository.existsByInterviewQuestionIdAndLikerId(interviewQuestionId, memberId)) {
            final DebugMessage debugMessage = DebugMessage.init()
                    .append("interviewQuestionId", interviewQuestionId)
                    .append("likerId", memberId);
            throw new InterviewQuestionLikesAlreadyExistException(debugMessage);
        }
    }
}
