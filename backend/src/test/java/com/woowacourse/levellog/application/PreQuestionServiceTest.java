package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.dto.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.Participant;
import com.woowacourse.levellog.team.domain.Team;
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
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            //when
            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), questioner.getId());

            //then
            final PreQuestion actual = preQuestionRepository.findById(id).orElseThrow();
            assertAll(
                    () -> assertThat(actual.getLevellog()).isEqualTo(levellog),
                    () -> assertThat(actual.getAuthor()).isEqualTo(questioner),
                    () -> assertThat(actual.getContent()).isEqualTo(preQuestion)
            );
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_FromNotParticipant_Exception() {
            // given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author);
            final Levellog levellog = saveLevellog(author, team);

            participantRepository.save(new Participant(team, author, true));

            // when, then
            assertThatThrownBy(() -> preQuestionService.save(preQuestionDto, levellog.getId(), questioner.getId()))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("같은 팀에 속한 멤버만 사전 질문을 작성할 수 있습니다.");
        }

        @Test
        @DisplayName("내 레벨로그에 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_LevellogIsMine_Exception() {
            // given
            final String preQuestion = "알린이 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(() -> preQuestionService.save(preQuestionDto, levellog.getId(), author.getId()))
                    .isInstanceOf(InvalidPreQuestionException.class)
                    .hasMessageContaining("자기 자신에게 사전 질문을 등록할 수 없습니다.");
        }

        @Test
        @DisplayName("사전 질문이 이미 등록되었을 때 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_PreQuestionAlreadyExist_Exception() {
            // given
            final String preQuestion = "알린이 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);

            // when, then
            assertThatThrownBy(() -> preQuestionService.save(preQuestionDto, levellog.getId(), questioner.getId()))
                    .isInstanceOf(PreQuestionAlreadyExistException.class)
                    .hasMessageContainingAll("사전 질문을 이미 작성하였습니다.",
                            String.valueOf(levellog.getId()),
                            String.valueOf(questioner.getId()));
        }
    }

    @Nested
    @DisplayName("findMy 메서드는")
    class FindMy {

        @Test
        @DisplayName("사전 질문을 조회한다.")
        void findMy() {
            //given
            final String content = "로마가 쓴 사전 질문";

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner, content);

            //when
            final PreQuestionDto response = preQuestionService.findMy(levellog.getId(), questioner.getId());

            //then
            assertThat(response.getPreQuestion()).isEqualTo(content);
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않으면 예외를 던진다.")
        void findMy_LevellogWrongId_Exception() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);

            // when, then
            assertThatThrownBy(() -> preQuestionService.findMy(999L, questioner.getId()))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContaining("레벨로그가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("사전 질문이 존재하지 않으면 예외를 던진다.")
        void findMy_PreQuestionNotFound_Exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(() -> preQuestionService.findMy(levellog.getId(), questioner.getId()))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContainingAll("사전 질문이 존재하지 않습니다.", "1");
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("사전 질문을 수정한다.")
        void update() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Long id = savePreQuestion(levellog, questioner).getId();

            //when
            preQuestionService.update(PreQuestionDto.from("수정된 사전 질문"), id, levellog.getId(), questioner.getId());

            //then
            final PreQuestionDto response = preQuestionService.findMy(levellog.getId(), questioner.getId());
            assertThat(response.getPreQuestion()).isEqualTo("수정된 사전 질문");
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_PreQuestionNotFound_Exception() {
            // given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionDto, 1L, levellog.getId(), questioner.getId()))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("타인의 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_FromNotMyPreQuestion_Exception() {
            // given
            final PreQuestionDto preQuestionDto = PreQuestionDto.from("로마가 쓴 사전 질문");

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(author, questioner, eve);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);
            final Long preQuestionId = savePreQuestion(levellog, eve).getId();

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionDto, preQuestionId, levellog.getId(),
                            questioner.getId()))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("자신의 사전 질문이 아닙니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 수정하면 예외를 던진다.")
        void update_LevellogWrongId_Exception() {
            //given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionDto preQuestionDto = PreQuestionDto.from(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Member author2 = saveMember("페퍼");
            final Team team2 = saveTeam(author2);
            final Levellog levellog2 = saveLevellog(author2, team2);

            final Long id = preQuestionService.save(preQuestionDto, levellog.getId(), questioner.getId());

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionDto, id, levellog2.getId(), questioner.getId()))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력한 levellogId와 사전 질문의 levellogId가 다릅니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("사전 질문을 삭제한다.")
        void deleteById() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Long id = savePreQuestion(levellog, questioner).getId();

            //when
            preQuestionService.deleteById(id, levellog.getId(), questioner.getId());

            //then
            assertThat(preQuestionRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("타인의 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_FromNotMyPreQuestion_Exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(author, questioner, eve);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);
            final Long preQuestionId = savePreQuestion(levellog, eve).getId();

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(preQuestionId, levellog.getId(), questioner.getId()))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("자신의 사전 질문이 아닙니다.");
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_PreQuestionNotFound_Exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(1L, levellog.getId(), questioner.getId()))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 삭제하면 예외를 던진다.")
        void deleteById_LevellogWrongId_Exception() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Member author2 = saveMember("페퍼");
            final Team team2 = saveTeam(author2);
            final Levellog levellog2 = saveLevellog(author2, team2);

            final Long id = savePreQuestion(levellog, questioner).getId();

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(id, levellog2.getId(), questioner.getId()))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("입력한 levellogId와 사전 질문의 levellogId가 다릅니다.");
        }
    }
}
