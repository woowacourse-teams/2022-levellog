package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.exception.InvalidLevellogException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotAuthorException;
import com.woowacourse.levellog.prequestion.domain.PreQuestion;
import com.woowacourse.levellog.prequestion.dto.request.PreQuestionWriteRequest;
import com.woowacourse.levellog.prequestion.dto.response.PreQuestionResponse;
import com.woowacourse.levellog.prequestion.exception.InvalidPreQuestionException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionAlreadyExistException;
import com.woowacourse.levellog.prequestion.exception.PreQuestionNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.exception.NotParticipantException;
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
        void success() {
            //given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            //when
            final Long id = preQuestionService.save(preQuestionWriteRequest, levellog.getId(),
                    getLoginStatus(questioner));

            //then
            final PreQuestion actual = preQuestionRepository.findById(id).orElseThrow();
            assertAll(
                    () -> assertThat(actual.getLevellog()).isEqualTo(levellog),
                    () -> assertThat(actual.getAuthorId()).isEqualTo(questioner.getId()),
                    () -> assertThat(actual.getContent()).isEqualTo(preQuestion)
            );
        }

        @Test
        @DisplayName("참가자가 아닌 멤버가 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_fromNotParticipant_exception() {
            // given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member teamMember = saveMember("이브");
            final Team team = saveTeam(author, teamMember);

            final Member questioner = saveMember("로마");
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.save(preQuestionWriteRequest, levellog.getId(),
                            getLoginStatus(questioner)))
                    .isInstanceOf(NotParticipantException.class)
                    .hasMessageContaining("팀 참가자가 아닙니다.");
        }

        @Test
        @DisplayName("내 레벨로그에 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_levellogIsMine_exception() {
            // given
            final String preQuestion = "알린이 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.save(preQuestionWriteRequest, levellog.getId(), getLoginStatus(author)))
                    .isInstanceOf(InvalidPreQuestionException.class)
                    .hasMessageContaining("잘못된 사전 질문 요청입니다.");
        }

        @Test
        @DisplayName("사전 질문이 이미 등록되었을 때 사전 질문을 등록하는 경우 예외를 던진다.")
        void save_preQuestionAlreadyExist_exception() {
            // given
            final String preQuestion = "알린이 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.save(preQuestionWriteRequest, levellog.getId(),
                            getLoginStatus(questioner)))
                    .isInstanceOf(PreQuestionAlreadyExistException.class)
                    .hasMessageContainingAll("사전 질문이 이미 존재합니다.",
                            String.valueOf(levellog.getId()),
                            String.valueOf(questioner.getId()));
        }
    }

    @Nested
    @DisplayName("findMy 메서드는")
    class FindMy {

        @Test
        @DisplayName("사전 질문을 조회한다.")
        void success() {
            //given
            final String content = "로마가 쓴 사전 질문";

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner, content);

            //when
            final PreQuestionResponse response = preQuestionService.findMy(levellog.getId(),
                    getLoginStatus(questioner));

            //then
            assertAll(
                    () -> assertThat(response.getContent()).isEqualTo(content),
                    () -> assertThat(response.getAuthor().getId()).isEqualTo(questioner.getId())
            );
        }

        @Test
        @DisplayName("레벨로그가 존재하지 않으면 예외를 던진다.")
        void findMy_levellogWrongId_exception() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);

            // when, then
            assertThatThrownBy(() -> preQuestionService.findMy(999L, getLoginStatus(author)))
                    .isInstanceOf(LevellogNotFoundException.class)
                    .hasMessageContaining("레벨로그가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("사전 질문이 존재하지 않으면 예외를 던진다.")
        void findMy_preQuestionNotFound_exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(() -> preQuestionService.findMy(levellog.getId(), getLoginStatus(author)))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContainingAll("사전 질문이 존재하지 않습니다.", String.valueOf(levellog.getId()));
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class Update {

        @Test
        @DisplayName("사전 질문을 수정한다.")
        void success() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Long id = savePreQuestion(levellog, questioner).getId();

            //when
            preQuestionService.update(new PreQuestionWriteRequest("수정된 사전 질문"), id, levellog.getId(),
                    getLoginStatus(questioner));

            //then
            preQuestionRepository.flush();
            final PreQuestionResponse response = preQuestionService.findMy(levellog.getId(),
                    getLoginStatus(questioner));
            assertThat(response.getContent()).isEqualTo("수정된 사전 질문");
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_preQuestionNotFound_exception() {
            // given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionWriteRequest, 1L, levellog.getId(),
                            getLoginStatus(author)))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("타인의 사전 질문을 수정하는 경우 예외를 던진다.")
        void update_fromNotMyPreQuestion_exception() {
            // given
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest("로마가 쓴 사전 질문");

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(author, questioner, eve);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);
            final Long preQuestionId = savePreQuestion(levellog, eve).getId();

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionWriteRequest, preQuestionId, levellog.getId(),
                            getLoginStatus(author)))
                    .isInstanceOf(MemberNotAuthorException.class)
                    .hasMessageContaining("작성자가 아닙니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 수정하면 예외를 던진다.")
        void update_levellogWrongId_exception() {
            //given
            final String preQuestion = "로마가 쓴 사전 질문";
            final PreQuestionWriteRequest preQuestionWriteRequest = new PreQuestionWriteRequest(preQuestion);

            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Member author2 = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team2 = saveTeam(author2, eve);
            final Levellog levellog2 = saveLevellog(author2, team2);

            final Long id = preQuestionService.save(preQuestionWriteRequest, levellog.getId(),
                    getLoginStatus(questioner));

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.update(preQuestionWriteRequest, id, levellog2.getId(),
                            getLoginStatus(author)))
                    .isInstanceOf(InvalidLevellogException.class)
                    .hasMessageContaining("잘못된 레벨로그 요청입니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("사전 질문을 삭제한다.")
        void success() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Long id = savePreQuestion(levellog, questioner).getId();

            //when
            preQuestionService.deleteById(id, levellog.getId(), getLoginStatus(questioner));

            //then
            assertThat(preQuestionRepository.existsById(id)).isFalse();
        }

        @Test
        @DisplayName("타인의 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_fromNotMyPreQuestion_exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Member eve = saveMember("이브");
            final Team team = saveTeam(author, questioner, eve);
            final Levellog levellog = saveLevellog(author, team);

            savePreQuestion(levellog, questioner);
            final Long preQuestionId = savePreQuestion(levellog, eve).getId();

            // when, then
            assertThatThrownBy(
                    () -> preQuestionService.deleteById(preQuestionId, levellog.getId(), getLoginStatus(author)))
                    .isInstanceOf(MemberNotAuthorException.class)
                    .hasMessageContaining("작성자가 아닙니다.");
        }

        @Test
        @DisplayName("저장되어있지 않은 사전 질문을 삭제하는 경우 예외를 던진다.")
        void deleteById_preQuestionNotFound_exception() {
            // given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(1L, levellog.getId(), getLoginStatus(author)))
                    .isInstanceOf(PreQuestionNotFoundException.class)
                    .hasMessageContaining("사전 질문이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("잘못된 레벨로그의 사전 질문을 삭제하면 예외를 던진다.")
        void deleteById_levellogWrongId_exception() {
            //given
            final Member author = saveMember("알린");
            final Member questioner = saveMember("로마");
            final Team team = saveTeam(author, questioner);
            final Levellog levellog = saveLevellog(author, team);

            final Member author2 = saveMember("페퍼");
            final Member eve = saveMember("이브");
            final Team team2 = saveTeam(author2, eve);
            final Levellog levellog2 = saveLevellog(author2, team2);

            final Long id = savePreQuestion(levellog, questioner).getId();

            // when, then
            assertThatThrownBy(() -> preQuestionService.deleteById(id, levellog2.getId(), getLoginStatus(author)))
                    .isInstanceOf(InvalidLevellogException.class)
                    .hasMessageContaining("잘못된 레벨로그 요청입니다.");
        }
    }
}
