package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    default Levellog getLevellog(final Long levellogId) {
        return findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    boolean existsByAuthorIdAndTeamId(Long authorId, Long teamId);

    @Query("SELECT l FROM Levellog l INNER JOIN FETCH l.team WHERE l.authorId = :authorId")
    List<Levellog> findAllByAuthorId(Long authorId);
}
