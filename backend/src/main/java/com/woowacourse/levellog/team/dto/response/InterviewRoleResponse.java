package com.woowacourse.levellog.team.dto.response;

import com.woowacourse.levellog.team.domain.InterviewRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewRoleResponse {

    private InterviewRole myRole;

    public static InterviewRoleResponse from(final InterviewRole interviewRole) {
        return new InterviewRoleResponse(interviewRole);
    }
}

