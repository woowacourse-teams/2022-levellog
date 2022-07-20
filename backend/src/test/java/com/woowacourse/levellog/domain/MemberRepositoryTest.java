package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.config.JpaConfig;
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
}
