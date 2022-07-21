package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.config.JpaConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("MemberRepository의")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("findByGithubId 메서드는 특정 GithubId가 포함된 멤버 객체를 반환한다.")
    void findByGithubId() {
        // given
        final Member member = memberRepository.save(new Member("nickname", 10, "123"));

        // when
        final Optional<Member> actual = memberRepository.findByGithubId(10);

        // then
        assertThat(actual).hasValue(member);
    }

    @Test
    @DisplayName("findAllByNicknameContains 메서드는 입력한 문자열이 포함된 nickname을 가진 멤버를 모두 조회한다.")
    void findAllByNicknameContains() {
        // given
        final Member roma = memberRepository.save(new Member("roma", 10, "roma.img"));
        final Member pepper = memberRepository.save(new Member("pepper", 20, "pepper.img"));
        final Member alien = memberRepository.save(new Member("alien", 30, "alien.img"));
        final Member rick = memberRepository.save(new Member("rick", 40, "rick.img"));
        final Member eve = memberRepository.save(new Member("eve", 50, "eve.img"));
        final Member kyul = memberRepository.save(new Member("kyul", 60, "kyul.img"));
        final Member harry = memberRepository.save(new Member("harry", 70, "harry.img"));

        // when
        final List<Member> actual = memberRepository.findAllByNicknameContains("ali");

        // then
        assertThat(actual).hasSize(1)
                .contains(alien);
    }
}
