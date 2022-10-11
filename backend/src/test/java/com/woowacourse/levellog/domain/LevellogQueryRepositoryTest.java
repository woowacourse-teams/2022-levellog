package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.dto.response.LevellogDetailResponse;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LevellogQueryRepository의")
class LevellogQueryRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findById 메서드는 levellogId에 해당하는 레벨로그를 조회한다.")
    void findById() {
        // given
        final Member author = saveMember("릭");
        final Member member = saveMember("로마");
        final Team team = saveTeam(author, member);
        final Levellog levellog = saveLevellog(author, team);

        // when
        final LevellogDetailResponse actual = levellogQueryRepository.findById(levellog.getId())
                .get();

        // then
        assertThat(actual).extracting(LevellogDetailResponse::getContent, it -> it.getAuthor().getId())
                .containsExactly(levellog.getContent(), author.getId());
    }
}
