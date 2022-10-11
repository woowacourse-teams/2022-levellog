package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
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

    public static InterviewQuestionLikes of(final InterviewQuestion interviewQuestion, final Long likerId) {
        return new InterviewQuestionLikes(interviewQuestion.getId(), likerId);
    }
}
