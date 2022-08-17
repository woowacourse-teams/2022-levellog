package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        final Member member = saveMember("릭", "깃허브_릭");
        final Integer githubId = member.getGithubId();

        // when
        final Optional<Member> actual = memberRepository.findByGithubId(githubId);

        // then
        assertThat(actual).hasValue(member);
    }

    @Test
    @DisplayName("findAllByNicknameOrGithubNicknameContains 메서드는 입력한 문자열이 포함된 nickname 또는 githubNickname을 가진 멤버를 모두 조회한다.")
    void findAllByNicknameOrGithubNicknameContains() {
        // given
        saveMember("roma", "github_roma");
        saveMember("pepper", "github_peper");
        saveMember("rick", "github_rick");
        saveMember("eve", "github_eve");
        saveMember("kyul", "github_kyul");
        saveMember("harry", "github_harry");

        final Member alien = saveMember("alien", "github_alien");

        // when
        final List<Member> actual1 = memberRepository.findAllByNicknameOrGithubNicknameContains("ali");
        final List<Member> actual2 = memberRepository.findAllByNicknameOrGithubNicknameContains("github_ali");

        // then
        assertAll(
                () -> assertThat(actual1).hasSize(1).contains(alien),
                () -> assertThat(actual2).hasSize(1).contains(alien)
        );
    }
}
