package com.woowacourse.levellog.teamdisplay.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ParticipantDetail {

    private final Long memberId;
    private final Long levellogId;
    private final Long preQuestionId;
    private final String nickname;
    private final String profileUrl;
}
