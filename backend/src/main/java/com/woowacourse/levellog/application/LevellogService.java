package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.Team;
import com.woowacourse.levellog.domain.TeamRepository;
import com.woowacourse.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
import com.woowacourse.levellog.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LevellogService {

    private final LevellogRepository levellogRepository;
    private final TeamRepository teamRepository;

    public Long save(final Member author, final Long groupId, final LevellogRequest request) {
        final Team team = getTeam(groupId);
        final Levellog levellog = new Levellog(author, team, request.getContent());

        final Levellog savedLevellog = levellogRepository.save(levellog);
        return savedLevellog.getId();
    }

    @Transactional(readOnly = true)
    public LevellogResponse findById(final Long id) {
        final Levellog levellog = getById(id);
        return new LevellogResponse(levellog.getContent());
    }

    public void update(final Long id, final LevellogRequest request) {
        final Levellog levellog = getById(id);
        levellog.updateContent(request.getContent());
    }

    public void deleteById(final Long id) {
        levellogRepository.deleteById(id);
    }

    private Levellog getById(final Long id) {
        return levellogRepository.findById(id).orElseThrow();
    }

    private Team getTeam(final Long groupId) {
        return teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
    }
}
