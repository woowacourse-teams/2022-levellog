package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewQuestionQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<InterviewQuestionSearchResultDto> searchRowMapper = (resultSet, rowNumber) -> new InterviewQuestionSearchResultDto(
            resultSet.getObject("id", Long.class),
            resultSet.getString("content"),
            resultSet.getBoolean("press"),
            resultSet.getInt("likeCount")
    );

    public InterviewQuestionQueryRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InterviewQuestionSearchResultDto> searchByKeyword(final String keyword, final Long memberId,
                                                                  final Long size, final Long page,
                                                                  final InterviewQuestionSort sort) {
        final String wrappedKeyword = "'%" + keyword + "%'";
        final String sql = "SELECT id, content, "
                + "(select CASE WHEN (id IN (select interview_question_id from interview_question_likes where liker_id = ?)) "
                + "THEN true ELSE false END ) AS press, "
                + "like_count AS likeCount "
                + "FROM interview_question "
                + "WHERE content LIKE ? "
                + String.format("ORDER BY %s %s ", sort.getField(), sort.getOrder())
                + "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, searchRowMapper, wrappedKeyword, memberId, size, page * size);
    }
}
