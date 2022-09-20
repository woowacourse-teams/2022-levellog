package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.AllSimpleParticipantDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TeamQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<AllSimpleParticipantDto> simpleRowMapper = (resultSet, rowNumber) -> new AllSimpleParticipantDto(
            resultSet.getObject("teamId", Long.class),
            resultSet.getString("title"),
            resultSet.getString("place"),
            resultSet.getTimestamp("start_at").toLocalDateTime(),
            resultSet.getString("teamProfileUrl"),
            resultSet.getBoolean("is_closed"),
            resultSet.getObject("memberId", Long.class),
            resultSet.getString("profile_url")
    );
    private final RowMapper<AllParticipantDto> detailRowMapper = (resultSet, rowNumber) -> new AllParticipantDto(
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

    public TeamQueryRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AllSimpleParticipantDto> findAllList(final boolean isClosed, final int limit, final int offset) {
        final String sql = "SELECT /*! STRAIGHT_JOIN */ "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.is_closed, "
                + "m.id memberId, m.profile_url "
                + "FROM "
                    + "(SELECT * "
                    + "FROM team "
                    + "WHERE deleted = FALSE AND is_closed = ? "
                    + "LIMIT ? OFFSET ?) AS t "
                    + "INNER JOIN participant p ON p.team_id = t.id AND p.is_watcher = FALSE "
                    + "INNER JOIN member m ON m.id = p.member_id "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        return jdbcTemplate.query(sql, simpleRowMapper, isClosed, limit, offset);
    }

    public List<AllParticipantDto> findAllByTeamId(final Long teamId, final Long memberId) {
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.interviewer_number, t.is_closed, t.created_at, t.updated_at, "
                + "m.id memberId, l.id levellogId, pq.id preQuestionId, m.nickname, m.profile_url, p.is_host, p.is_watcher "
                + "FROM participant p "
                    + "INNER JOIN member m ON p.member_id = m.id "
                    + "INNER JOIN team t ON p.team_id = t.id "
                    + "LEFT OUTER JOIN levellog l ON p.team_id = l.team_id AND l.author_id = m.id "
                    + "LEFT OUTER JOIN pre_question pq ON pq.levellog_id = l.id AND pq.author_id = ? "
                + "WHERE t.deleted = false AND t.id = ? "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        return jdbcTemplate.query(sql, detailRowMapper, memberId, teamId);
    }

    public List<AllSimpleParticipantDto> findMyList(final Member member) {
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.is_closed, t.created_at, "
                + "m.id memberId, m.profile_url "
                + "FROM participant p "
                    + "INNER JOIN member m ON p.member_id = m.id "
                    + "INNER JOIN team t ON p.team_id = t.id "
                + "WHERE t.id IN ( "
                    + "SELECT "
                    + "t.id FROM participant p "
                    + "INNER JOIN member m ON p.member_id = m.id "
                    + "INNER JOIN team t ON p.team_id = t.id "
                    + "WHERE m.id = ? AND deleted = FALSE) "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        return jdbcTemplate.query(sql, simpleRowMapper, member.getId());
    }
}
