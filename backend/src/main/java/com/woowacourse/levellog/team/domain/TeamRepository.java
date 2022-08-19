package com.woowacourse.levellog.team.domain;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByIsClosed(boolean isClosed, Sort sort);
}
