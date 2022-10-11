package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    boolean existsByAuthorIdAndTeamId(Long authorId, Long teamId);

    @Query("SELECT l FROM Levellog l INNER JOIN FETCH l.team WHERE l.authorId = :authorId")
    List<Levellog> findAllByAuthorId(@Param("authorId") Long authorId);

    default Levellog getLevellog(final Long levellogId) {
        return findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }
}
