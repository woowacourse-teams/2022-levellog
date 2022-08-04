package com.woowacourse.levellog.levellog.domain;

<<<<<<< HEAD
import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
=======
>>>>>>> main
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevellogRepository extends JpaRepository<Levellog, Long> {

    Optional<Levellog> findByAuthorIdAndTeamId(Long authorId, Long teamId);
<<<<<<< HEAD

    boolean existsByAuthorIdAndTeamId(Long authorId, Long teamId);

    List<Levellog> findAllByAuthor(Member author);
=======
>>>>>>> main
}
