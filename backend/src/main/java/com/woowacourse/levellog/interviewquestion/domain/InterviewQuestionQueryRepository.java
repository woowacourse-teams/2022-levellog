package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionContentDto;
import com.woowacourse.levellog.interviewquestion.dto.InterviewQuestionSearchResultDto;
import com.woowacourse.levellog.interviewquestion.dto.SimpleInterviewQuestionDto;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.dto.MemberDto;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewQuestionQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<InterviewQuestionSearchResultDto> searchRowMapper = (resultSet, rowNum) -> new InterviewQuestionSearchResultDto(
            resultSet.getObject("id", Long.class),
            resultSet.getString("content"),
            resultSet.getBoolean("press"),
            resultSet.getInt("likeCount")
    );
    private final RowMapper<SimpleInterviewQuestionDto> interviewQuestionRowMapper = (resultSet, rowNum) -> new SimpleInterviewQuestionDto(
            new MemberDto(
                    resultSet.getObject("authorId", Long.class),
                    resultSet.getString("nickname"),
                    resultSet.getString("profileUrl")
            ),
            new InterviewQuestionContentDto(
                    resultSet.getObject("interviewQuestionId", Long.class),
                    resultSet.getString("content")
            )
    );

    public InterviewQuestionQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InterviewQuestionSearchResultDto> searchByKeyword(final String keyword, final Long memberId,
                                                                  final Long size, final Long page,
                                                                  final InterviewQuestionSort sort) {
        final String sql = "SELECT id, content, "
                + "(select CASE WHEN (id IN (select interview_question_id from interview_question_likes where liker_id = :memberId)) "
                + "THEN true ELSE false END ) AS press, "
                + "like_count AS likeCount "
                + "FROM interview_question "
                + "WHERE content LIKE '%" + keyword + "%' "
                + String.format("ORDER BY %s %s ", sort.getField(), sort.getOrder())
                + "LIMIT :limit OFFSET :offset";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("limit", size)
                .addValue("offset", page * size);
        return jdbcTemplate.query(sql, param, searchRowMapper);
    }

    public List<SimpleInterviewQuestionDto> findAllByLevellog(final Levellog levellog) {
        final String sql = "SELECT m.id authorId, m.nickname, m.profile_url profileUrl, "
                + "iq.id interviewQuestionId, iq.content "
                + "FROM interview_question iq "
                + "INNER JOIN member m on iq.author_id = m.id "
                + "WHERE iq.levellog_id = :levellogId";

        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("levellogId", levellog.getId());
        return jdbcTemplate.query(sql, param, interviewQuestionRowMapper);
    }
}
