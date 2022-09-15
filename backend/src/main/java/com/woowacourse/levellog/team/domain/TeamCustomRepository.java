package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
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

    public List<AllParticipantDto> findAllMy(final Member member) {
        final String jpql = "SELECT new com.woowacourse.levellog.team.dto.AllParticipantDto("
                + "p.team, m.id, l.id, pq.id, m.nickname, m.profileUrl, p.isHost, p.isWatcher) "
                + "FROM Participant p "
                + "INNER JOIN Member m ON p.member = m "
                + "LEFT OUTER JOIN Levellog l ON p.team = l.team AND l.author = m "
                + "LEFT OUTER JOIN PreQuestion pq ON pq.levellog = l AND pq.author = :member "
                + "WHERE p.team.deleted = false AND p.team IN "
                    + "(SELECT t "
                    + "FROM Participant p "
                    + "INNER JOIN Member m ON p.member = m "
                    + "INNER JOIN Team t ON p.team = t "
                    + "WHERE m = :member) "
                + "ORDER BY p.team.isClosed ASC, p.team.createdAt DESC";

        return em.createQuery(jpql, AllParticipantDto.class)
                .setParameter("member", member)
                .getResultList();
    }
}
