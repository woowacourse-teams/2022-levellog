package com.woowacourse.levellog.prequestion.application;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.domain.PreQuestionRepository;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreQuestionService {

    private final PreQuestionRepository preQuestionRepository;
    private final LevellogRepository levellogRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(final PreQuestionDto request, final Long levellogId, final Long memberId) {
        final Levellog levellog = getLevellog(levellogId);
        final Member from = getMember(memberId);

        return preQuestionRepository.save(request.toEntity(levellog, from))
                .getId();
    }

    public PreQuestionDto findById(final Long preQuestionId, final Long levellogId) {
        final PreQuestion preQuestion = getPreQuestion(preQuestionId);
        validateLevellog(preQuestion, levellogId);

        return PreQuestionDto.from(preQuestion);
    }

    @Transactional
    public void update(final PreQuestionDto request, final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = getPreQuestionByFromMember(preQuestionId, memberId);
        validateLevellog(preQuestion, levellogId);

        preQuestion.update(request.getPreQuestion());
    }

    @Transactional
    public void deleteById(final Long preQuestionId, final Long levellogId, final Long memberId) {
        final PreQuestion preQuestion = getPreQuestionByFromMember(preQuestionId, memberId);
        validateLevellog(preQuestion, levellogId);

        preQuestionRepository.deleteById(
                preQuestion.getId());
    }

    private PreQuestion getPreQuestion(final Long preQuestionId) {
        return preQuestionRepository.findById(preQuestionId)
                .orElseThrow(() -> new PreQuestionNotFoundException(
                        "사전 질문이 존재하지 않습니다. preQuestionId : " + preQuestionId, "사전 질문이 존재하지 않습니다."));
    }

    private PreQuestion getPreQuestionByFromMember(final Long preQuestionId, final Long memberId) {
        return preQuestionRepository.findByIdAndFrom(preQuestionId, getMember(memberId))
                .orElseThrow(() -> new PreQuestionNotFoundException(
                        "작성한 사전 질문이 존재하지 않습니다. preQuestionId : " + preQuestionId + " memberId : " + memberId,
                        "작성한 사전 질문이 존재하지 않습니다."));
    }

    private Levellog getLevellog(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException("레벨로그가 존재하지 않습니다. levellogId : " + levellogId));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private void validateLevellog(final PreQuestion preQuestion, final Long levellogId) {
        if (!preQuestion.isSameLevellog(getLevellog(levellogId))) {
            throw new InvalidFieldException("입력한 levellogId와 사전 질문의 levellogId가 다릅니다. 입력한 levellogId : " + levellogId);
        }
    }
}
