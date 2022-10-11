package com.woowacourse.levellog.feedback.domain;

import com.woowacourse.levellog.feedback.dto.request.FeedbackContentRequest;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponse;
import com.woowacourse.levellog.feedback.dto.response.FeedbackResponses;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<FeedbackResponse> feedbackRowMapper = (resultSet, rowNum) -> new FeedbackResponse(
            resultSet.getLong("feedbackId"),
            new MemberResponse(
                    resultSet.getLong("fromId"),
                    resultSet.getString("fromNickname"),
                    resultSet.getString("fromProfileUrl")
            ),
            new MemberResponse(
                    resultSet.getLong("toId"),
                    resultSet.getString("toNickname"),
                    resultSet.getString("toProfileUrl")
            ),
            new FeedbackContentRequest(
                    resultSet.getString("study"),
                    resultSet.getString("speak"),
                    resultSet.getString("etc")
            ),
            resultSet.getTimestamp("updatedAt").toLocalDateTime()
    );

    public FeedbackQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FeedbackResponses findAllByLevellogId(final Long levellogId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id "
                + "WHERE f.levellog_id = :levellogId";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("levellogId", levellogId);

        final List<FeedbackResponse> feedbacks = jdbcTemplate.query(sql, param, feedbackRowMapper);
        return new FeedbackResponses(feedbacks);
    }

    public FeedbackResponses findAllByTo(final Long memberId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id AND tm.id = :memberId "
                + "ORDER BY f.updated_at DESC";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId);

        final List<FeedbackResponse> feedbacks = jdbcTemplate.query(sql, param, feedbackRowMapper);
        return new FeedbackResponses(feedbacks);
    }

    public Optional<FeedbackResponse> findById(final Long feedbackId) {
        final String sql = "SELECT f.id feedbackId, f.study, f.speak, f.etc, f.updated_at updatedAt, "
                + "fm.id fromId, fm.nickname fromNickname, fm.profile_url fromProfileUrl, "
                + "tm.id toId, tm.nickname toNickname, tm.profile_url toProfileUrl "
                + "FROM feedback f "
                + "INNER JOIN member fm ON f.from_id = fm.id "
                + "INNER JOIN member tm ON f.to_id = tm.id "
                + "WHERE f.id = :feedbackId";
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("feedbackId", feedbackId);

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, param, feedbackRowMapper));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
