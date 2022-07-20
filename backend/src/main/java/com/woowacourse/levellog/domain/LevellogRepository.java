package com.woowacourse.levellog.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    Optional<Levellog> findByAuthorId(Long authorId);
}
