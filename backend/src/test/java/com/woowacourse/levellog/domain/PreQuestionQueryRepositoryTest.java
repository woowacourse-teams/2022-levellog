package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.dto.response.PreQuestionResponse;
import com.woowacourse.levellog.team.domain.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PreQuestionQueryRepository의")
class PreQuestionQueryRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByLevellogAndAuthor 메서드는 Levellog와 Author가 같은 사전 질문을 반환한다.")
    void findByLevellogAndAuthor() {
        // given
        final Member levellogAuthor = saveMember("알린");
        final Member questioner = saveMember("로마");
        final Team team = saveTeam(levellogAuthor, questioner);
        final Levellog levellog = saveLevellog(levellogAuthor, team);

        final PreQuestion preQuestion = savePreQuestion(levellog, questioner);

        // when
        final PreQuestionResponse actual = preQuestionQueryRepository.findByLevellogAndAuthor(levellog.getId(),
                questioner.getId()).get();

        // then
        assertThat(actual).extracting(it -> it.getAuthor().getId(), PreQuestionResponse::getContent)
                .containsExactly(questioner.getId(), preQuestion.getContent());
    }
}
