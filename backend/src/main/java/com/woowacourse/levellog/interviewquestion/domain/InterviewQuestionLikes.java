package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.interviewquestion.exception.InvalidInterviewQuestionException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.InvalidMemberException;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_interview_question_id_liker_id",
                columnNames = {"interviewQuestionId", "likerId"}
        )})
public class InterviewQuestionLikes extends BaseEntity {

    private Long interviewQuestionId;
    private Long likerId;

    private InterviewQuestionLikes(final Long interviewQuestionId, final Long likerId) {
        this.interviewQuestionId = interviewQuestionId;
        this.likerId = likerId;
    }

    public static InterviewQuestionLikes of(final InterviewQuestion interviewQuestion, final Member liker) {
        validateInterviewQuestion(interviewQuestion);
        validateLiker(liker);

        return new InterviewQuestionLikes(interviewQuestion.getId(), liker.getId());
    }

    private static void validateInterviewQuestion(InterviewQuestion interviewQuestion) {
        if (interviewQuestion.getId() == null) {
            throw new InvalidInterviewQuestionException(DebugMessage.init()
                    .append("interviewQuestionId", interviewQuestion.getId()));
        }
    }

    private static void validateLiker(Member liker) {
        if (liker.getId() == null) {
            throw new InvalidMemberException(DebugMessage.init()
                    .append("memberId", liker.getId()));
        }
    }
}
