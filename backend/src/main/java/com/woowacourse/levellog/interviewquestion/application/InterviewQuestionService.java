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
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultsDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionWriteDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionsDto;
import com.woowacourse.levellog.interviewquestion.dto.SimpleInterviewQuestionDto;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikeNotFoundException;
import com.woowacourse.levellog.interviewquestion.exception.InterviewQuestionLikesAlreadyExistException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.team.domain.ParticipantRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.ParticipantNotSameTeamException;
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
    private final ParticipantRepository participantRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final InterviewQuestionWriteDto request, final Long levellogId,
                     @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Team team = levellog.getTeam();

        levellog.validateSelfInterviewQuestion(loginStatus.getMemberId());
        validateMemberIsParticipant(loginStatus.getMemberId(), levellog);
        team.validateInProgress(timeStandard.now());

        final InterviewQuestion interviewQuestion = request.toInterviewQuestion(loginStatus.getMemberId(), levellog);

        return interviewQuestionRepository.save(interviewQuestion)
                .getId();
    }

    public InterviewQuestionsDto findAllByLevellog(final Long levellogId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final List<InterviewQuestionDto> interviewQuestions = interviewQuestionQueryRepository.findAllByLevellog(
                        levellog)
                .stream()
                .collect(Collectors.groupingBy(
                                SimpleInterviewQuestionDto::getAuthor,
                                LinkedHashMap::new,
                                Collectors.mapping(SimpleInterviewQuestionDto::getContent, Collectors.toList())
                        )
                ).entrySet()
                .stream()
                .map(it -> new InterviewQuestionDto(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        return new InterviewQuestionsDto(interviewQuestions);
    }

    public InterviewQuestionContentsDto findAllByLevellogAndAuthor(final Long levellogId,
                                                                   @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final List<InterviewQuestion> interviewQuestions = interviewQuestionRepository.findAllByLevellogAndAuthorId(
                levellog, loginStatus.getMemberId());

        return InterviewQuestionContentsDto.from(interviewQuestions);
    }

    public InterviewQuestionSearchResultsDto searchByKeyword(final String keyword,
                                                             @Verified final LoginStatus loginStatus,
                                                             final Long size, final Long page, final String sort) {
        final InterviewQuestionSort sortCondition = InterviewQuestionSort.valueOf(sort.toUpperCase());
        final List<InterviewQuestionSearchResultDto> results = interviewQuestionQueryRepository.searchByKeyword(keyword,
                loginStatus, size, page, sortCondition);

        return InterviewQuestionSearchResultsDto.of(results, page);
    }

    @Transactional
    public void update(final InterviewQuestionWriteDto request, final Long interviewQuestionId,
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

        interviewQuestionLikesRepository.save(InterviewQuestionLikes.of(interviewQuestion, loginStatus.getMemberId()));
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

    private void validateMemberIsParticipant(final Long memberId, final Levellog levellog) {
        final Team team = levellog.getTeam();

        if (!participantRepository.existsByMemberIdAndTeam(memberId, team)) {
            throw new ParticipantNotSameTeamException(DebugMessage.init()
                    .append("teamId", team.getId())
                    .append("memberId", memberId)
                    .append("levellogId", levellog.getId()));
        }
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
