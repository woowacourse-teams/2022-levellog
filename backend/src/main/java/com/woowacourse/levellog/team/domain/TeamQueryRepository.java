package com.woowacourse.levellog.team.domain;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.team.dto.AllParticipantDto;
import com.woowacourse.levellog.team.dto.AllSimpleParticipantDto;
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
            resultSet.getObject("memberId", Long.class),
            resultSet.getObject("levellogId", Long.class),
            resultSet.getObject("preQuestionId", Long.class),
            resultSet.getString("nickname"),
            resultSet.getBoolean("is_host"),
            resultSet.getBoolean("is_watcher")
    );

    public TeamQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AllSimpleParticipantDto> findAllList(final boolean isClosed, final int limit, final int offset) {
        final String sql = "SELECT /*! STRAIGHT_JOIN */ "
                + "t.id teamId, t.title, t.place, t.start_at, t.profile_url teamProfileUrl, t.is_closed, "
                + "m.id memberId, m.profile_url "
                + "FROM "
                    + "(SELECT * "
                    + "FROM team "
                    + "WHERE deleted = FALSE AND is_closed = :isClosed "
                    + "ORDER BY created_at DESC "
                    + "LIMIT :limit OFFSET :offset) AS t "
                + "INNER JOIN participant p ON p.team_id = t.id AND p.is_watcher = FALSE "
                + "INNER JOIN member m ON m.id = p.member_id "
                + "ORDER BY t.created_at DESC";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("isClosed", isClosed)
                .addValue("limit", limit)
                .addValue("offset", offset);
        return jdbcTemplate.query(sql, param, simpleRowMapper);
    }

    public List<AllParticipantDto> findAllByTeamId(final Long teamId, final LoginStatus loginStatus) {
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
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

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
                    + "WHERE m.id = :memberId AND deleted = FALSE) "
                + "ORDER BY t.is_closed ASC, t.created_at DESC";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", member.getId());
        return jdbcTemplate.query(sql, param, simpleRowMapper);
    }
}
