package com.woowacourse.levellog.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.levellog.team.dto.ParticipantIdsDto;
import com.woowacourse.levellog.team.dto.TeamCreateDto;
import com.woowacourse.levellog.team.dto.TeamUpdateDto;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("TeamController의")
class TeamControllerTest extends ControllerTest {

    @Nested
    @DisplayName("save 메서드는")
    class save {

        // TODO: Authorization 헤더 추가

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("팀 명으로 공백이나 null이 들어오면 예외를 던진다.")
        void titleNullOrEmpty_Exception(final String title) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto request = new TeamCreateDto(title, "트랙룸", LocalDateTime.now().plusDays(3), participantIds);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("인터뷰 장소로 공백이나 null이 들어오면 예외를 던진다.")
        void placeNullOrEmpty_Exception(final String place) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", place, LocalDateTime.now().plusDays(3), participantIds);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", "트랙룸", null, participantIds);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("팀 구성원 목록으로 null이 들어오면 예외를 던진다.")
        void participantsNull_Exception() throws Exception {
            // given
            final TeamCreateDto request = new TeamCreateDto("잠실 준조", "트랙룸", LocalDateTime.now().plusDays(3), null);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("update 메서드는")
    class update {

        // TODO: Authorization 헤더 추가

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("팀 명으로 공백이나 null이 들어오면 예외를 던진다.")
        void titleNullOrEmpty_Exception(final String title) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), participantIds);
            final String teamRequestContent = objectMapper.writeValueAsString(teamCreateDto);
            mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(teamRequestContent))
                    .andDo(print());

            final TeamUpdateDto request = new TeamUpdateDto(title, "트랙룸", LocalDateTime.now().plusDays(3));
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(put("/api/teams/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {" "})
        @NullAndEmptySource
        @DisplayName("인터뷰 장소로 공백이나 null이 들어오면 예외를 던진다.")
        void placeNullOrEmpty_Exception(final String place) throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), participantIds);
            final String teamRequestContent = objectMapper.writeValueAsString(teamCreateDto);
            mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(teamRequestContent))
                    .andDo(print());

            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", place, LocalDateTime.now().plusDays(3));
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("인터뷰 시작 시간으로 null이 들어오면 예외를 던진다.")
        void startAtNull_Exception() throws Exception {
            // given
            final ParticipantIdsDto participantIds = new ParticipantIdsDto(List.of(1L, 2L));
            final TeamCreateDto teamCreateDto = new TeamCreateDto("잠실 네오조", "트랙룸", LocalDateTime.now().plusDays(3), participantIds);
            final String teamRequestContent = objectMapper.writeValueAsString(teamCreateDto);
            mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(teamRequestContent))
                    .andDo(print());

            final TeamUpdateDto request = new TeamUpdateDto("잠실 제이슨조", "트랙룸", null);
            final String requestContent = objectMapper.writeValueAsString(request);

            // when
            final ResultActions perform = mockMvc.perform(post("/api/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestContent))
                    .andDo(print());

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
