package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MemberRepository의")
class MemberRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByGithubId 메서드는 특정 GithubId가 포함된 멤버 객체를 반환한다.")
    void findByGithubId() {
        // given
        final Member member = saveMember("릭");
        final Integer githubId = member.getGithubId();

        // when
        final Optional<Member> actual = memberRepository.findByGithubId(githubId);

        // then
        assertThat(actual).hasValue(member);
    }

    @Test
    @DisplayName("findAllByNicknameContains 메서드는 입력한 문자열이 포함된 nickname을 가진 멤버를 모두 조회한다.")
    void findAllByNicknameContains() {
        // given
        saveMember("roma");
        saveMember("pepper");
        saveMember("rick");
        saveMember("eve");
        saveMember("kyul");
        saveMember("harry");

        final Member alien = saveMember("alien");

        // when
        final List<Member> actual = memberRepository.findAllByNicknameContains("ali");

        // then
        assertThat(actual).hasSize(1)
                .contains(alien);
    }

    @Nested
    @DisplayName("getMember 메서드는")
    class GetMember {

        @Test
        @DisplayName("memberId에 해당하는 레코드가 존재하면 id에 해당하는 Member 엔티티를 반환한다.")
        void success() {
            // given
            final Long expected = saveMember("릭")
                    .getId();

            // when
            final Long actual = memberRepository.getMember(expected)
                    .getId();

            // then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("memberId에 해당하는 레코드가 존재하지 않으면 예외를 던진다.")
        void getMember_notExist_exception() {
            // given
            final Long memberId = 999L;

            // when & then
            assertThatThrownBy(() -> memberRepository.getMember(memberId))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}
