package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    default Levellog getLevellog(final Long levellogId) {
        return findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    default Levellog getLevellogWithTeamAndParticipantsById(final Long levellogId) {
        return findLevellogWithTeamAndParticipantsById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);

    boolean existsByAuthorIdAndTeamId(Long authorId, Long teamId);

    @Query("SELECT l FROM Levellog l INNER JOIN FETCH l.team WHERE l.author = :author")
    List<Levellog> findAllByAuthor(@Param("author") Member author);

    @Query("SELECT l FROM Levellog l INNER JOIN FETCH l.author WHERE l.id = :id")
    Optional<Levellog> findLevellogAndMemberByLevelogId(@Param("id") Long id);

    @Query("SELECT l FROM Levellog l "
            + "INNER JOIN FETCH l.team t "
            + "INNER JOIN FETCH t.participants p "
            + "WHERE l.id = :levellogId")
    Optional<Levellog> findLevellogWithTeamAndParticipantsById(@Param("levellogId") Long levellogId);
}
