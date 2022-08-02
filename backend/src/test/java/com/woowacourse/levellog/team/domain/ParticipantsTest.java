package com.woowacourse.levellog.team.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.exception.ParticipantNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Participants의")
class ParticipantsTest {

    Member getMember(final String nickname, final Long memberId) {
        final Member member = new Member(nickname, ((int) System.nanoTime()), nickname + ".com");

        return MockEntityFactory.setId(memberId, member);
    }

    private List<Long> toIdList(final String s) {
        return Arrays.stream(s.split(", "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Nested
    @DisplayName("toInterviewerIds 메서드는")
    class toInterviewerIds {

        @ParameterizedTest(name = "인터뷰어 수가 {0}명이고 참가자 아이디가 [1, 2, 3, 4, 5]인 팀에서 아이디가 {1}인 참가자의 인터뷰어 아이디는 [{2}]이다.")
        @CsvSource(value = {"2:1:2, 3", "2:4:5, 1", "4:3:4, 5, 1, 2"}, delimiter = ':')
        void success(final int interviewerNumber, final Long targetMemberId, final String expectedIds) {
            // given
            final List<Long> expected = toIdList(expectedIds);
            final Team team = new Team();
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );

            final Participants participants = new Participants(values);

            // when
            final List<Long> actual = participants.toInterviewerIds(targetMemberId, interviewerNumber);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(interviewerNumber),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

        @Test
        @DisplayName("타겟 멤버가 참가자가 아니면 빈 리스트를 반환한다.")
        void emptyList() {
            // given
            final Team team = new Team();
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );

            final Participants participants = new Participants(values);

            // when
            final List<Long> actual = participants.toInterviewerIds(999L, 1);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("toIntervieweeIds 메서드는")
    class toIntervieweeIds {

        @ParameterizedTest(name = "인터뷰어 수가 {0}명이고 참가자 아이디가 [1, 2, 3, 4, 5]인 팀에서 아이디가 {1}인 참가자의 인터뷰이 아이디는 [{2}]이다.")
        @CsvSource(value = {"2:1:4, 5", "2:4:2, 3", "4:3:4, 5, 1, 2"}, delimiter = ':')
        void success(final int interviewerNumber, final Long targetMemberId, final String expectedIds) {
            // given
            final List<Long> expected = toIdList(expectedIds);
            final Team team = new Team();
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );

            final Participants participants = new Participants(values);

            // when
            final List<Long> actual = participants.toIntervieweeIds(targetMemberId, interviewerNumber);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(interviewerNumber),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

        @Test
        @DisplayName("타겟 멤버가 참가자가 아니면 빈 리스트를 반환한다.")
        void emptyList() {
            // given
            final Team team = new Team();
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );

            final Participants participants = new Participants(values);

            // when
            final List<Long> actual = participants.toInterviewerIds(999L, 1);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    @DisplayName("toInterviewRole 메서드는")
    class toInterviewRole {

        @ParameterizedTest(name = "참가자 아이디가 [1, 2, 3, 4, 5]이고 인터뷰어 수가 2명인 팀에서 아이디가 {0}인 타겟 멤버에 대해 아이디가 {1}인 참가자의 인터뷰 역할은 {2}이다.")
        @CsvSource(value = {"1,2,INTERVIEWER", "1,3,INTERVIEWER", "1,4,OBSERVER", "1,5,OBSERVER"})
        void success(final Long targetMemberId, final Long memberId, final InterviewRole expected) {
            // given
            final Team team = MockEntityFactory.setId(1L, new Team());
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );
            final Participants participants = new Participants(values);

            // when
            final InterviewRole actual = participants.toInterviewRole(team.getId(), targetMemberId, memberId, 2);

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("타겟 멤버가 요청한 멤버와 같으면 ME를 반환한다.")
        void me() {
            // given
            final Team team = MockEntityFactory.setId(1L, new Team());
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );
            final Participants participants = new Participants(values);

            // when
            final InterviewRole actual = participants.toInterviewRole(team.getId(), 1L, 1L, 2);

            // then
            assertThat(actual).isEqualTo(InterviewRole.ME);
        }

        @Test
        @DisplayName("요청한 멤버가 참가자가 아니라면 예외를 던진다.")
        void requestMemberIdNotContains_exceptionThrown() {
            // given
            final Team team = MockEntityFactory.setId(1L, new Team());
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );
            final Participants participants = new Participants(values);

            // when & then
            assertThatThrownBy(() -> participants.toInterviewRole(team.getId(), 1L, 9L, 2))
                    .isInstanceOf(UnauthorizedException.class);
        }

        @Test
        @DisplayName("타겟 멤버가 참가자가 아니라면 예외를 던진다.")
        void targetMemberIdNotContains_exceptionThrown() {
            // given
            final Team team = MockEntityFactory.setId(1L, new Team());
            final List<Participant> values = new ArrayList<>(
                    List.of(
                            new Participant(team, getMember("릭", 1L), true),
                            new Participant(team, getMember("로마", 2L), false),
                            new Participant(team, getMember("알린", 3L), false),
                            new Participant(team, getMember("이브", 4L), false),
                            new Participant(team, getMember("해리", 5L), false)
                    )
            );
            final Participants participants = new Participants(values);

            // when & then
            assertThatThrownBy(() -> participants.toInterviewRole(team.getId(), 9L, 1L, 2))
                    .isInstanceOf(ParticipantNotFoundException.class);
        }
    }
}
