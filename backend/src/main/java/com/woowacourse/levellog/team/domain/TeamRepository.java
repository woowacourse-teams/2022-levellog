package com.woowacourse.levellog.team.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findAllByIsClosed(boolean isClosed, Pageable pageable);
}
