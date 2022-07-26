package com.woowacourse.levellog.levellog.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);
}
