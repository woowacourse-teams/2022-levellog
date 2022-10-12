package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.member.dto.response.MemberResponse;
import com.woowacourse.levellog.prequestion.dto.response.PreQuestionResponse;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class PreQuestionQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<PreQuestionResponse> preQuestionRowMapper = (resultSet, rowNum) -> new PreQuestionResponse(
            new MemberResponse(
                    resultSet.getLong("authorId"),
                    resultSet.getString("nickname"),
                    resultSet.getString("profileUrl")
            ),
            resultSet.getString("content")
    );

    public PreQuestionQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PreQuestionResponse> findByLevellogAndAuthor(final Long levellogId, final Long authorId) {
        final String sql = "SELECT m.id authorId, m.nickname, m.profile_url profileUrl, pq.content "
                + "FROM pre_question pq "
                + "INNER JOIN member m ON pq.author_id = m.id "
                + "WHERE pq.levellog_id = :levellogId AND pq.author_id = :authorId";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("levellogId", levellogId)
                .addValue("authorId", authorId);

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, param, preQuestionRowMapper));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
