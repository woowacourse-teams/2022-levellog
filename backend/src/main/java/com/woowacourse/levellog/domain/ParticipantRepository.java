package com.woowacourse.levellog.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByTeam(Team team);

    void deleteByTeam(Team team);

    boolean existsByMemberAndTeam(Member member, Team team);
}
