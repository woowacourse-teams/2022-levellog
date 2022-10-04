package com.woowacourse.levellog.team.dto.query;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllSimpleParticipantQueryResult {

    private Long id;
    private String title;
    private String place;
    private LocalDateTime startAt;
    private String teamImage;
    private boolean isClosed;
    private Long memberId;
    private String memberImage;
}
