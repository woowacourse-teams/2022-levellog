package com.woowacourse.levellog.levellog.domain;

import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class LevellogQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<LevellogDto> levellogRowMapper = (resultSet, rowNum) -> new LevellogDto(
            new MemberDto(
                    resultSet.getLong("authorId"),
                    resultSet.getString("nickname"),
                    resultSet.getString("profileUrl")
            ),
            resultSet.getString("content")
    );

    public LevellogQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<LevellogDto> findById(final Long levellogId) {
        final String sql = "SELECT m.id authorId, m.nickname, m.profile_url profileUrl, l.content "
                + "FROM levellog l "
                + "INNER JOIN member m on l.author_id = m.id "
                + "WHERE l.id = :levellogId";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("levellogId", levellogId);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, param, levellogRowMapper));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
