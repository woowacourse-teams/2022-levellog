package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionQueryResult;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("InterviewQuestionQueryRepository의")
class InterviewQuestionQueryRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findAllByLevellog 메서드는")
    void findAllByLevellog() {
        // given
        final Member rick = saveMember("릭");
        final Member pepper = saveMember("페퍼");
        final Member roma = saveMember("로마");

        final Team team = saveTeam(rick, pepper, roma);

        final Levellog levellog = saveLevellog(rick, team);

        saveInterviewQuestion("로마가 씀 1", levellog, roma);
        saveInterviewQuestion("페퍼가 씀", levellog, pepper);
        saveInterviewQuestion("로마가 씀 2", levellog, roma);
        saveInterviewQuestion("로마가 씀 3", levellog, roma);

        // when
        final List<InterviewQuestionQueryResult> actual = interviewQuestionQueryRepository.findAllByLevellog(levellog);

        // then
        assertThat(actual).hasSize(4)
                .extracting(it -> it.getAuthor().getId(), it -> it.getContent().getContent())
                .containsExactly(
                        tuple(roma.getId(), "로마가 씀 1"),
                        tuple(pepper.getId(), "페퍼가 씀"),
                        tuple(roma.getId(), "로마가 씀 2"),
                        tuple(roma.getId(), "로마가 씀 3")
                );
    }
}
