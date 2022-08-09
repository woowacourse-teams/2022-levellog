package com.woowacourse.levellog.member.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewNicknameTableRepository extends JpaRepository<CrewNicknameTable, Long> {

    Optional<CrewNicknameTable> findByGithubNickname(String githubNickname);
}
