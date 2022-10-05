package com.woowacourse.levellog.team.dto.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ParticipantSimpleQueryResult {

    private Long memberId;
    private String profileUrl;

    public static ParticipantSimpleQueryResult from(
            final AllTeamListQueryResult allTeamListQueryResult) {
        return new ParticipantSimpleQueryResult(
                allTeamListQueryResult.getMemberId(),
                allTeamListQueryResult.getMemberImage()
        );
    }
}
