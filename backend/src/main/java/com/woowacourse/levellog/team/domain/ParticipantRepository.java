package com.woowacourse.levellog.team.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByMemberIdAndTeam(Long memberId, Team team);

    List<Participant> findByTeam(Team team);
}
