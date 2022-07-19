package com.woowacourse.levellog.dto;

import com.woowacourse.levellog.domain.Participant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantIdsRequest extends Participant {

    private List<Long> participantIds;
}
