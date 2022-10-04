package com.woowacourse.levellog.team.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p FROM Participant p WHERE p.team = :team")
    List<Participant> findByTeam(@Param("team") Team team);

    void deleteByTeam(Team team);

    boolean existsByMemberIdAndTeam(Long memberId, Team team);
}
