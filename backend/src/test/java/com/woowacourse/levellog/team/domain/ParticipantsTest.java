package com.woowacourse.levellog.team.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.common.domain.MockEntityFactory;
import com.woowacourse.levellog.member.domain.Member;
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
}
