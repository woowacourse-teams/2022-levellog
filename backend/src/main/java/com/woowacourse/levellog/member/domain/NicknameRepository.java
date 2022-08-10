package com.woowacourse.levellog.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameRepository extends JpaRepository<Nickname, Long> {

    Optional<Nickname> findByGithubNickname(String githubNickname);
}
