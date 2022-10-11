package com.woowacourse.levellog.interviewquestion.domain;

import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.query.InterviewQuestionSearchQueryResult;
import com.woowacourse.levellog.interviewquestion.dto.response.InterviewQuestionContentResponse;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.dto.response.MemberResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewQuestionQueryRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<InterviewQuestionSearchQueryResult> searchRowMapper = (resultSet, rowNum) -> new InterviewQuestionSearchQueryResult(
            resultSet.getLong("id"),
            resultSet.getString("content"),
            resultSet.getBoolean("press"),
            resultSet.getInt("likeCount")
    );

    private final RowMapper<InterviewQuestionQueryResult> interviewQuestionRowMapper = (resultSet, rowNum) -> new InterviewQuestionQueryResult(
            new MemberResponse(
                    resultSet.getLong("authorId"),
                    resultSet.getString("nickname"),
                    resultSet.getString("profileUrl")
            ),
            new InterviewQuestionContentResponse(
                    resultSet.getLong("interviewQuestionId"),
                    resultSet.getString("content")
            )
    );

    public InterviewQuestionQueryRepository(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<InterviewQuestionSearchQueryResult> searchByKeyword(final String keyword, final LoginStatus loginStatus,
                                                                    final Long size, final Long page,
                                                                    final InterviewQuestionSort sort) {
        final String sql = "SELECT id, content, "
                + createLikerCondition(loginStatus)
                + "like_count AS likeCount "
                + "FROM interview_question "
                + "WHERE content LIKE '%" + keyword + "%' "
                + String.format("ORDER BY %s %s ", sort.getField(), sort.getOrder())
                + "LIMIT :limit OFFSET :offset";

        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("limit", size);
        paramMap.put("offset", page * size);
        if (loginStatus.isLogin()) {
            paramMap.put("memberId", loginStatus.getMemberId());
        }

        final SqlParameterSource param = new MapSqlParameterSource(paramMap);
        return jdbcTemplate.query(sql, param, searchRowMapper);
    }

    private String createLikerCondition(final LoginStatus loginStatus) {
        if (loginStatus.isLogin()) {
            return "(select CASE WHEN (id IN (select interview_question_id from interview_question_likes where liker_id = :memberId)) "
                    + "THEN true ELSE false END ) AS press, ";
        }
        return "FALSE AS press, ";
    }

    public List<InterviewQuestionQueryResult> findAllByLevellog(final Levellog levellog) {
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
