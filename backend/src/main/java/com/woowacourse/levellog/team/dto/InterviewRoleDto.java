package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.InterviewRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class InterviewRoleDto {

    private InterviewRole myRole;

    public static InterviewRoleDto from(final InterviewRole interviewRole) {
        return new InterviewRoleDto(interviewRole);
    }
}

