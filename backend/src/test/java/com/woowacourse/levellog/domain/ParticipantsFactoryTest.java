package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.common.exception.InvalidFieldException;
import com.woowacourse.levellog.team.domain.ParticipantsFactory;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipantsFactoryTest {

    @Nested
    @DisplayName("createParticipants 메서드는")
    class CreateParticipants {

        @Test
        @DisplayName("참가자가 존재하지 않을 경우 예외를 발생시킨다.")
        void validate_participantExistence_exception() {
            // given
            final List<Long> participantIds = List.of();
            final List<Long> watcherIds = List.of(1L, 2L);

            // when & then
            assertThatThrownBy(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 1L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자는 1명 이상이어야 합니다.");
        }

        @Test
        @DisplayName("중복된 참가자가 존재할 경우 예외를 발생시킨다.")
        void validate_distinctParticipant_exception() {
            // given
            final List<Long> participantIds = List.of(1L, 2L, 2L);
            final List<Long> watcherIds = List.of(3L, 4L);

            // when & then
            assertThatThrownBy(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 1L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참가자가 존재합니다.");
        }

        @Test
        @DisplayName("중복된 참관자가 존재할 경우 예외를 발생시킨다.")
        void validate_distinctWatcher_exception() {
            // given
            final List<Long> participantIds = List.of(1L, 2L);
            final List<Long> watcherIds = List.of(3L, 3L, 4L);

            // when & then
            assertThatThrownBy(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 1L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("중복된 참관자가 존재합니다.");
        }

        @Test
        @DisplayName("참가자와 참관자에 모두 포함된 멤버 ID가 존재할 경우 예외를 발생시킨다.")
        void validate_independent_exception() {
            // given
            final List<Long> participantIds = List.of(1L, 2L);
            final List<Long> watcherIds = List.of(2L, 3L);

            // when & then
            assertThatThrownBy(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 1L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자와 참관자에 모두 포함된 멤버가 존재합니다.");
        }

        @Test
        @DisplayName("호스트의 ID가 참가자 또는 참관자 목록에 존재하지 않을 경우 예외를 발생시킨다.")
        void validate_hostExistence_exception() {
            // given
            final List<Long> participantIds = List.of(1L, 2L);
            final List<Long> watcherIds = List.of(3L, 4L);

            // when & then
            assertThatThrownBy(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 5L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("호스트가 참가자 또는 참관자 목록에 존재하지 않습니다.");
        }

        @Test
        @DisplayName("참가자 수가 인터뷰어 수보다 많지 않을 경우 예외를 발생시킨다.")
        void validate_participantNumber_exception() {
            // given
            final List<Long> participantIds = List.of(1L, 2L);
            final List<Long> watcherIds = List.of(3L);

            // when & then
            assertThatCode(
                    () -> ParticipantsFactory.createParticipants(TeamTest.saveTeam(), 1L, participantIds, watcherIds))
                    .isInstanceOf(InvalidFieldException.class)
                    .hasMessageContaining("참가자 수는 인터뷰어 수 보다 많아야 합니다.");
        }
    }
}
