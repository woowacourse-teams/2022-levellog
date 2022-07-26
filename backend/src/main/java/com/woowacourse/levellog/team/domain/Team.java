package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.domain.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false, length = 2048)
    private String profileUrl;

    public Team(final String title, final String place, final LocalDateTime startAt, final String profileUrl) {
        this.title = title;
        this.place = place;
        this.startAt = startAt;
        this.profileUrl = profileUrl;
    }

    public void update(final String title, final String place, final LocalDateTime startAt) {
        this.title = title;
        this.place = place;
        this.startAt = startAt;
    }
}
