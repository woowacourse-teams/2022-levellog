package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.dto.PreQuestionCreateDto;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PreQuestionService의")
public class PreQuestionServiceTest extends ServiceTest {

    @Test
    @DisplayName("save 메서드는 팀을 생성한다.")
    void save() {
        //given
        final String preQuestion = "알린의 사전질문";
        final PreQuestionCreateDto preQuestionCreateDto = new PreQuestionCreateDto(preQuestion);

        final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
        final Team team = teamRepository.save(new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
        final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

        final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));

        //when
        final Long id = preQuestionService.save(preQuestionCreateDto, levellog.getId(), from.getId());

        //then
        final PreQuestion actual = preQuestionRepository.findById(id).orElseThrow();
        assertAll(
                () -> assertThat(actual.getLevellog()).isEqualTo(levellog),
                () -> assertThat(actual.getFrom()).isEqualTo(from),
                () -> assertThat(actual.getPreQuestion()).isEqualTo(preQuestion)
        );
    }
}
