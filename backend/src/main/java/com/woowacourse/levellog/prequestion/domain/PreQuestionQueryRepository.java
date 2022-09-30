package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.prequestion.dto.PreQuestionDto;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PreQuestionQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PreQuestionDto> preQuestionRowMapper = (resultSet, rowNum) -> new PreQuestionDto(
            new MemberDto(
                    resultSet.getObject("authorId", Long.class),
                    resultSet.getString("nickname"),
                    resultSet.getString("profileUrl")
            ),
            resultSet.getString("content")
    );

    public PreQuestionQueryRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<PreQuestionDto> findByLevellogAndAuthor(final Long levellogId, final Long authorId) {
        final String sql = "SELECT m.id authorId, m.nickname, m.profile_url profileUrl, pq.content "
                + "FROM pre_question pq "
                + "INNER JOIN member m ON pq.author_id = m.id "
                + "WHERE pq.levellog_id = ? AND pq.author_id = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, preQuestionRowMapper, levellogId, authorId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
