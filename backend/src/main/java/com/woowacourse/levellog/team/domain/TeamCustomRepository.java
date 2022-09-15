package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.team.dto.ParticipantDto;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TeamCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public TeamCustomRepository(final EntityManager em) {
        this.em = em;
    }

    public List<ParticipantDto> findAllParticipants(final Team team, final Long memberId) {
        final String jpql = "SELECT new com.woowacourse.levellog.team.dto.ParticipantDto("
                + "m.id, l.id, pq.id, m.nickname, m.profileUrl) "
                + "FROM Participant p "
                + "INNER JOIN Member m ON p.member = m "
                + "LEFT OUTER JOIN Levellog l ON p.team = l.team AND l.author = m "
                + "LEFT OUTER JOIN PreQuestion pq ON pq.levellog = l AND pq.author.id = :memberId "
                + "WHERE p.team = :team AND p.isWatcher = false ";

        return em.createQuery(jpql, ParticipantDto.class)
                .setParameter("memberId", memberId)
                .setParameter("team", team)
                .getResultList();
    }

    public List<AllParticipantDto> findAll(final Long memberId) {
        final String jpql = "SELECT new com.woowacourse.levellog.team.dto.AllParticipantDto("
                + "p.team, m.id, l.id, pq.id, m.nickname, m.profileUrl, p.isHost, p.isWatcher) "
                + "FROM Participant p "
                + "INNER JOIN Member m ON p.member = m "
                + "LEFT OUTER JOIN Levellog l ON p.team = l.team AND l.author = m "
                + "LEFT OUTER JOIN PreQuestion pq ON pq.levellog = l AND pq.author.id = :memberId "
                + "WHERE p.team.deleted = false "
                + "ORDER BY p.team.isClosed ASC, p.team.createdAt DESC";

        return em.createQuery(jpql, AllParticipantDto.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
