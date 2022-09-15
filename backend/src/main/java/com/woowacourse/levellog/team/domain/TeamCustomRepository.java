package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.ParticipantDto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TeamCustomRepository {

    @PersistenceContext
    private EntityManager em;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AllParticipantDto> rowMapper = (resultSet, rowNumber) -> new AllParticipantDto(
            resultSet.getObject("teamId", Long.class),
            resultSet.getString("title"),
            resultSet.getString("place"),
            resultSet.getTimestamp("start_at").toLocalDateTime(),
            resultSet.getString("teamProfileUrl"),
            resultSet.getInt("interviewer_number"),
            resultSet.getBoolean("is_closed"),
            resultSet.getTimestamp("created_at").toLocalDateTime(),
            resultSet.getTimestamp("updated_at").toLocalDateTime(),
            resultSet.getObject("memberId", Long.class),
            resultSet.getObject("levellogId", Long.class),
            resultSet.getObject("preQuestionId", Long.class),
            resultSet.getString("nickname"),
            resultSet.getString("profile_url"),
            resultSet.getBoolean("is_host"),
            resultSet.getBoolean("is_watcher")
    );

    public TeamCustomRepository(final EntityManager em, final JdbcTemplate jdbcTemplate) {
        this.em = em;
        this.jdbcTemplate = jdbcTemplate;
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
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.interviewer_number, t.is_closed, t.created_at, t.updated_at, "
                + "m.id memberId, l.id levellogId, pq.id preQuestionId, m.nickname, m.profile_url, p.is_host, p.is_watcher "
                + "FROM participant p "
                + "INNER JOIN member m ON p.member_id = m.id "
                + "INNER JOIN team t ON p.team_id = t.id "
                + "LEFT OUTER JOIN levellog l ON p.team_id = l.team_id AND l.author_id = m.id "
                + "LEFT OUTER JOIN pre_question pq ON pq.levellog_id = l.id AND pq.author_id = ? "
                + "WHERE t.deleted = false "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<AllParticipantDto> findAllMy(final Member member) {
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.interviewer_number, t.is_closed, t.created_at, t.updated_at, "
                + "m.id memberId, l.id levellogId, pq.id preQuestionId, m.nickname, m.profile_url, p.is_host, p.is_watcher "
                + "FROM participant p "
                + "INNER JOIN member m ON p.member_id = m.id "
                + "INNER JOIN team t ON p.team_id = t.id "
                + "LEFT OUTER JOIN levellog l ON p.team_id = l.team_id AND l.author_id = m.id "
                + "LEFT OUTER JOIN pre_question pq ON pq.levellog_id = l.id AND pq.author_id = ? "
                + "WHERE t.deleted = false AND t.id IN "
                + "(SELECT t.id "
                + "FROM participant p "
                + "INNER JOIN member m ON p.member_id = m.id "
                + "INNER JOIN team t ON p.team_id = t.id "
                + "WHERE m.id = ?) "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        return jdbcTemplate.query(sql, rowMapper, member.getId(), member.getId());
    }
}
