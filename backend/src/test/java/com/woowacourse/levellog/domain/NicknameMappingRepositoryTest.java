package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.member.domain.NicknameMapping;
import com.woowacourse.levellog.member.domain.NicknameMappingRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("NicknameMappingRepository의")
public class NicknameMappingRepositoryTest {

    @Autowired
    NicknameMappingRepository nicknameMappingRepository;

    @Test
    @DisplayName("findByGithubNickname 메서드는 특정 GithubNickname이 포함된 닉네임 객체를 반환한다.")
    void findByGithubNickname() {
        // given
        final NicknameMapping nicknameMapping = nicknameMappingRepository.save(new NicknameMapping("깃허브로마", "우테코로마"));

        // when
        final Optional<NicknameMapping> actual = nicknameMappingRepository.findByGithubNickname("깃허브로마");

        // then
        assertThat(actual).hasValue(nicknameMapping);
    }
}
