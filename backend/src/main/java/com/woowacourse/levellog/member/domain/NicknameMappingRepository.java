package com.woowacourse.levellog.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NicknameMappingRepository extends JpaRepository<NicknameMapping, Long> {

    Optional<NicknameMapping> findByGithubNickname(String githubNickname);
}
