package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("SELECT p FROM Participant p INNER JOIN FETCH p.member WHERE p.team = :team")
    List<Participant> findByTeam(@Param("team") Team team);

    void deleteByTeam(Team team);

    boolean existsByMemberAndTeam(Member member, Team team);

    List<Participant> findAllByMember(Member member);
}
