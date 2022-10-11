package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogQueryRepository;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.levellog.dto.response.LevellogListResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogWithIdResponse;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LevellogService {

    private final LevellogRepository levellogRepository;
    private final LevellogQueryRepository levellogQueryRepository;
    private final TeamRepository teamRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final LevellogWriteRequest request, @Verified final LoginStatus loginStatus, final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        validateLevellogExistence(loginStatus.getMemberId(), teamId);
        team.validateReady(timeStandard.now());

        final Levellog savedLevellog = levellogRepository.save(request.toLevellog(loginStatus.getMemberId(), team));

        return savedLevellog.getId();
    }

    public LevellogResponse findById(final Long levellogId) {
        return levellogQueryRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    public LevellogListResponse findAllByAuthorId(@Verified final LoginStatus loginStatus) {
        final List<Levellog> levellogs = levellogRepository.findAllByAuthorId(loginStatus.getMemberId());
        final List<LevellogWithIdResponse> levellogWithIdResponses = levellogs.stream()
                .map(LevellogWithIdResponse::from)
                .collect(Collectors.toList());

        return new LevellogListResponse(levellogWithIdResponses);
    }

    @Transactional
    public void update(final LevellogWriteRequest request, final Long levellogId,
                       @Verified final LoginStatus loginStatus) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        levellog.getTeam().validateReady(timeStandard.now());

        levellog.updateContent(loginStatus.getMemberId(), request.getContent());
    }

    private void validateLevellogExistence(final Long authorId, final Long teamId) {
        final boolean isExists = levellogRepository.existsByAuthorIdAndTeamId(authorId, teamId);
        if (isExists) {
            throw new LevellogAlreadyExistException(DebugMessage.init()
                    .append("authorId", authorId)
                    .append("teamId", teamId));
        }
    }
}
