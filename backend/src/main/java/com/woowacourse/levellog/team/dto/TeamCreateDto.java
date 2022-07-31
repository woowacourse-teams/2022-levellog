package com.woowacourse.levellog.team.dto;

import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String place;

    @Positive
    private int interviewerNumber;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private ParticipantIdsDto participants;

    @Deprecated(since = "인터뷰 참가자 역할 부여 기능 이후", forRemoval = true)
    public TeamCreateDto(final String title, final String place, final LocalDateTime startAt,
                         final ParticipantIdsDto participants) {
        this.title = title;
        this.place = place;
        this.interviewerNumber = 1;
        this.startAt = startAt;
        this.participants = participants;
    }

    public Team toEntity(final String profileUrl) {
        return new Team(title, place, startAt, profileUrl, interviewerNumber);
    }
}
