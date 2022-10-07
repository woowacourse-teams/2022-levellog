package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.feedback.dto.FeedbackContentDto;
import com.woowacourse.levellog.feedback.dto.FeedbackDto;
import com.woowacourse.levellog.feedback.dto.FeedbacksDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<FeedbackDto> feedbackRowMapper = (resultSet, rowNum) -> new FeedbackDto(
            resultSet.getObject("feedbackId", Long.class),
            new MemberDto(
                    resultSet.getObject("fromId", Long.class),
                    resultSet.getString("fromNickname"),
                    resultSet.getString("fromProfileUrl")
            ),
            new MemberDto(
                    resultSet.getObject("toId", Long.class),
                    resultSet.getString("toNickname"),
                    resultSet.getString("toProfileUrl")
            ),
            new FeedbackContentDto(
                    resultSet.getString("study"),
                    resultSet.getString("speak"),
                    resultSet.getString("etc")
            ),
            resultSet.getTimestamp("updatedAt").toLocalDateTime()
    );

    public FeedbackQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FeedbacksDto findAllByLevellogId(final Long levellogId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id "
                + "WHERE f.levellog_id = :levellogId";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("levellogId", levellogId);

        final List<FeedbackDto> feedbacks = jdbcTemplate.query(sql, param, feedbackRowMapper);
        return new FeedbacksDto(feedbacks);
    }

    public FeedbacksDto findAllByTo(final Long memberId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id AND tm.id = :memberId "
                + "ORDER BY f.updated_at DESC";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        final List<FeedbackDto> feedbacks = jdbcTemplate.query(sql, param, feedbackRowMapper);
        return new FeedbacksDto(feedbacks);
    }

    public FeedbackDto findById(final Long feedbackId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id "
                + "WHERE f.id = :feedbackId";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("feedbackId", feedbackId);

        return jdbcTemplate.queryForObject(sql, param, feedbackRowMapper);
    }
}
