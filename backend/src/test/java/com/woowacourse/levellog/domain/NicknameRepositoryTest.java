package com.woowacourse.levellog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.common.config.JpaConfig;
import com.woowacourse.levellog.member.domain.Nickname;
import com.woowacourse.levellog.member.domain.NicknameRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
@DisplayName("NicknameRepository의")
public class NicknameRepositoryTest {

    @Autowired
    NicknameRepository nicknameRepository;

    @Test
    @DisplayName("findByGithubNickname 메서드는 특정 GithubNickname이 포함된 닉네임 객체를 반환한다.")
    void findByGithubNickname() {
        // given
        final Nickname nickname = nicknameRepository.save(
                new Nickname("깃허브로마", "우테코로마"));

        // when
        final Optional<Nickname> actual = nicknameRepository.findByGithubNickname("깃허브로마");

        // then
        assertThat(actual).hasValue(nickname);
    }
}
