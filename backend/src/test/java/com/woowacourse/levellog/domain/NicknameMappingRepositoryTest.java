package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.member.domain.NicknameMapping;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NicknameMappingRepository의")
class NicknameMappingRepositoryTest extends RepositoryTest {

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
