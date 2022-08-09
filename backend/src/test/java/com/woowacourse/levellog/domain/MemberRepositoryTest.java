package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MemberRepository의")
class MemberRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("findByGithubId 메서드는 특정 GithubId가 포함된 멤버 객체를 반환한다.")
    void findByGithubId() {
        // given
        final Member member = getMember("릭");
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
        getMember("roma");
        getMember("pepper");
        getMember("rick");
        getMember("eve");
        getMember("kyul");
        getMember("harry");

        final Member alien = getMember("alien");

        // when
        final List<Member> actual = memberRepository.findAllByNicknameContains("ali");

        // then
        assertThat(actual).hasSize(1)
                .contains(alien);
    }
}
