package com.woowacourse.levellog.team.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipantInTeamListResponse {

    private Long memberId;
    private String nickname;
    private String profileUrl;
}
