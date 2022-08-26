package com.woowacourse.levellog.teamdisplay.domain;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.domain.Team;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamDisplayRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<ParticipantDetail> findAllParticipantDetail(final Member member, final Team team) {
        final String jpql = "SELECT new com.woowacourse.levellog.teamdisplay.domain.ParticipantDetail("
                + "p.member.id, "
                + "l.id, "
                + "pq.id, "
                + "p.member.nickname, "
                + "p.member.profileUrl) "
                + "FROM Participant p "
                + "LEFT JOIN Levellog l ON p.team.id = l.team.id AND p.member.id = l.author.id "
                + "LEFT JOIN PreQuestion pq ON l.id = pq.levellog.id AND pq.author = :member "
                + "WHERE p.team = :team AND p.isWatcher = false";

        return entityManager.createQuery(jpql, ParticipantDetail.class)
                .setParameter("member", member)
                .setParameter("team", team)
                .getResultList();
    }
}
