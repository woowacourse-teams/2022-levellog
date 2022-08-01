package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PreQuestionService의")
public class PreQuestionServiceTest extends ServiceTest {

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("사전 질문을 생성한다.")
        void save() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            //when
            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            //then
            final PreQuestion actual = preQuestionRepository.findById(id).orElseThrow();
            assertAll(
                    () -> assertThat(actual.getLevellog()).isEqualTo(levellog),
                    () -> assertThat(actual.getFrom()).isEqualTo(from),
                    () -> assertThat(actual.getPreQuestion()).isEqualTo(preQuestion)
            );
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 등록하는 경우 예외를 던진다.")
        void fromUnauthorized_Exception() {
            // given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));

            // when, then
            assertThatThrownBy(() -> preQuestionService.save(preQuestionDto, levellog.getId(), from.getId()))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("같은 팀에 속한 멤버만 사전 질문을 작성할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        @Test
        @DisplayName("사전 질문을 조회한다.")
        void findById() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            //when
            final PreQuestionDto response = preQuestionService.findById(id, levellog.getId());

            //then
            assertThat(response.getPreQuestion()).isEqualTo(preQuestion);
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 조회하면 예외를 던진다.")
        void levellogWrongId_Exception() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            final Team team2 = teamRepository.save(
                    new Team("잠실 브라운조", "수성방", LocalDateTime.now().plusDays(3), "브라운조.img"));
            final Member author2 = memberRepository.save(new Member("페퍼", 12341234, "페퍼.img"));
            final Levellog levellog2 = levellogRepository.save(Levellog.of(author2, team2, "페퍼의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            // when, then
            assertThatThrownBy(() -> preQuestionService.findById(id, levellog2.getId()))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력한 levellogId와 사전 질문의 levellogId가 다릅니다.");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("사전 질문을 수정한다.")
        void update() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            //when
            preQuestionService.update(new PreQuestionDto("수정된 사전 질문"), id, levellog.getId(), from.getId());

            //then
            final PreQuestionDto response = preQuestionService.findById(id, levellog.getId());
            assertThat(response.getPreQuestion()).isEqualTo("수정된 사전 질문");
        }

        @Test
        @DisplayName("사전 질문이 없는데 사전 질문을 수정하는 경우 예외를 던진다.")
        void fromNotFound_Exception() {
            // given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            // when, then
            assertThatThrownBy(() -> preQuestionService.update(preQuestionDto, 1L, levellog.getId(), from.getId()))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("작성한 사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 수정하면 예외를 던진다.")
        void levellogWrongId_Exception() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            final Team team2 = teamRepository.save(
                    new Team("잠실 브라운조", "수성방", LocalDateTime.now().plusDays(3), "브라운조.img"));
            final Member author2 = memberRepository.save(new Member("페퍼", 12341234, "페퍼.img"));
            final Levellog levellog2 = levellogRepository.save(Levellog.of(author2, team2, "페퍼의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            // when, then
            assertThatThrownBy(() -> preQuestionService.update(preQuestionDto, id, levellog2.getId(), from.getId()))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력한 levellogId와 사전 질문의 levellogId가 다릅니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("사전 질문을 삭제한다.")
        void update() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            //when
            preQuestionService.deleteById(id, levellog.getId(), from.getId());

            //then
            assertThat(preQuestionRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 삭제하는 경우 예외를 던진다.")
        void fromUnauthorized_Exception() {
            // given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(1L, levellog.getId(), from.getId()))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("작성한 사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 조회하면 예외를 던진다.")
        void levellogWrongId_Exception() {
            //given
            final String preQuestion = "알린의 사전질문";
            final PreQuestionDto preQuestionDto = new PreQuestionDto(preQuestion);

            final Team team = teamRepository.save(
                    new Team("선릉 네오조", "목성방", LocalDateTime.now().plusDays(3), "네오조.img"));
            final Member author = memberRepository.save(new Member("알린", 12345678, "알린.img"));
            final Member from = memberRepository.save(new Member("로마", 56781234, "로마.img"));
            final Levellog levellog = levellogRepository.save(Levellog.of(author, team, "알린의 레벨로그"));

            final Team team2 = teamRepository.save(
                    new Team("잠실 브라운조", "수성방", LocalDateTime.now().plusDays(3), "브라운조.img"));
            final Member author2 = memberRepository.save(new Member("페퍼", 12341234, "페퍼.img"));
            final Levellog levellog2 = levellogRepository.save(Levellog.of(author2, team2, "페퍼의 레벨로그"));

            participantRepository.save(new Participant(team, author, true));
            participantRepository.save(new Participant(team, from, false));

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), from.getId());

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(id, levellog2.getId(), from.getId()))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력한 levellogId와 사전 질문의 levellogId가 다릅니다.");
        }
    }
}
