package com.woowacourse.levellog.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LevellogRepository extends JpaRepository<Levellog, Long> {
}
