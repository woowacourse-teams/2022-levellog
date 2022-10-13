package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.authentication.support.Verified;
import com.woowacourse.levellog.common.dto.LoginStatus;
import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogQueryRepository;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.levellog.dto.response.LevellogDetailResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponses;
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
        final Long authorId = loginStatus.getMemberId();
        final Team team = teamRepository.getTeam(teamId);

        team.validateReady(timeStandard.now());
        team.validateIsParticipants(authorId);
        validateLevellogExistence(authorId, teamId);

        final Levellog savedLevellog = levellogRepository.save(request.toLevellog(authorId, team));

        return savedLevellog.getId();
    }

    public LevellogDetailResponse findById(final Long levellogId) {
        return levellogQueryRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    public LevellogResponses findAllByAuthorId(@Verified final LoginStatus loginStatus) {
        final List<Levellog> levellogs = levellogRepository.findAllByAuthorId(loginStatus.getMemberId());
        final List<LevellogResponse> levellogResponses = levellogs.stream()
                .map(it -> new LevellogResponse(it.getId(), it.getContent()))
                .collect(Collectors.toList());

        return new LevellogResponses(levellogResponses);
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
