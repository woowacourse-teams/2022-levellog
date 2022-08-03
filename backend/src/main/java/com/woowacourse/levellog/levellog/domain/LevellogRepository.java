package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);

    boolean existsByAuthorIdAndTeamId(Long authorId, Long teamId);

    List<Levellog> findAllByAuthor(Member author);
}
