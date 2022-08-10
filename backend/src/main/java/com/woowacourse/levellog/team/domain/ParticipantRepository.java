package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findByTeam(Team team);

    void deleteByTeam(Team team);

    boolean existsByMemberAndTeam(Member member, Team team);

    List<Participant> findAllByMember(Member member);
}
