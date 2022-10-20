package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.team.dto.query.TeamDetailQueryResult;
import com.woowacourse.levellog.team.dto.query.TeamListQueryResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class TeamQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<TeamListQueryResult> simpleRowMapper = (resultSet, rowNumber) -> new TeamListQueryResult(
            resultSet.getLong("teamId"),
            resultSet.getString("title"),
            resultSet.getString("place"),
            resultSet.getTimestamp("start_at").toLocalDateTime(),
            resultSet.getString("teamProfileUrl"),
            resultSet.getBoolean("is_closed"),
            resultSet.getLong("memberId"),
            resultSet.getString("nickname"),
            resultSet.getString("profile_url")
    );
    private final RowMapper<TeamDetailQueryResult> detailRowMapper = (resultSet, rowNumber) -> new TeamDetailQueryResult(
            resultSet.getLong("teamId"),
            resultSet.getString("title"),
            resultSet.getString("place"),
            resultSet.getTimestamp("start_at").toLocalDateTime(),
            resultSet.getString("teamProfileUrl"),
            resultSet.getInt("interviewer_number"),
            resultSet.getBoolean("is_closed"),
            resultSet.getLong("memberId"),
            resultSet.getObject("levellogId", Long.class),
            resultSet.getObject("preQuestionId", Long.class),
            resultSet.getString("nickname"),
            resultSet.getString("profile_url"),
            resultSet.getBoolean("is_host"),
            resultSet.getBoolean("is_watcher")
    );

    public TeamQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TeamListQueryResult> findAllList(final boolean isClosed, final int limit, final int offset) {
        final String sql = "SELECT /*! STRAIGHT_JOIN */ "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.is_closed, "
                + "m.id memberId, m.nickname, m.profile_url "
                + "FROM "
                + "(SELECT * "
                + "FROM team "
                + "WHERE deleted = FALSE AND is_closed = :isClosed "
                + "ORDER BY created_at DESC "
                + "LIMIT :limit OFFSET :offset) AS t "
                + "INNER JOIN participant p ON p.team_id = t.id AND p.is_watcher = FALSE "
                + "INNER JOIN member m ON m.id = p.member_id "
                + "ORDER BY t.created_at DESC, p.id ASC";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("isClosed", isClosed)
                .addValue("limit", limit)
                .addValue("offset", offset);
        return jdbcTemplate.query(sql, param, simpleRowMapper);
    }

    public List<TeamDetailQueryResult> findAllByTeamId(final Long teamId, final LoginStatus loginStatus) {
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.interviewer_number, t.is_closed, "
                + "m.id memberId, l.id levellogId, m.nickname, m.profile_url, p.is_host, p.is_watcher "
                + createPreQuestionScala(loginStatus)
                + "FROM participant p "
                + "INNER JOIN member m ON p.member_id = m.id "
                + "INNER JOIN team t ON p.team_id = t.id "
                + "LEFT OUTER JOIN levellog l ON p.team_id = l.team_id AND l.author_id = m.id "
                + createPreQuestionJoinSql(loginStatus)
                + "WHERE t.deleted = false AND t.id = :teamId "
                + "ORDER BY p.id ASC";

        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teamId", teamId);
        if (loginStatus.isLogin()) {
            paramMap.put("authorId", loginStatus.getMemberId());
        }

        final SqlParameterSource param = new MapSqlParameterSource(paramMap);
        return jdbcTemplate.query(sql, param, detailRowMapper);
    }

    private String createPreQuestionJoinSql(final LoginStatus loginStatus) {
        if (loginStatus.isLogin()) {
            return "LEFT OUTER JOIN pre_question pq ON pq.levellog_id = l.id AND pq.author_id = :authorId ";
        }
        return "";
    }

    private String createPreQuestionScala(final LoginStatus loginStatus) {
        if (loginStatus.isLogin()) {
            return ", pq.id preQuestionId ";
        }
        return ", NULL preQuestionId ";
    }

    public List<TeamListQueryResult> findMyList(final Long memberId) {
        final String sql = "SELECT "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.is_closed, "
                + "m.id memberId, m.nickname, m.profile_url "
                + "FROM (SELECT t.* "
                    + "FROM team t INNER JOIN participant p ON p.team_id = t.id "
                    + "WHERE t.deleted = FALSE AND p.member_id = :memberId "
                    + "AND p.is_watcher = FALSE) AS t "
                + "LEFT OUTER JOIN participant p ON p.team_id = t.id AND is_watcher = FALSE "
                + "LEFT OUTER JOIN member m ON p.member_id = m.id "
                + "ORDER BY t.is_closed ASC, t.created_at DESC, p.id ASC";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        return jdbcTemplate.query(sql, param, simpleRowMapper);
    }
}
