package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.dto.LevellogCreateRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LevellogService {

    private final LevellogRepository levellogRepository;

    public Long save(final LevellogCreateRequest request) {
        final Levellog levellog = new Levellog(request.getContent());

        final Levellog savedLevellog = levellogRepository.save(levellog);
        return savedLevellog.getId();
    }

    @Transactional(readOnly = true)
    public LevellogResponse find(final Long id) {
        final Levellog levellog = getLevellog(id);
        return new LevellogResponse(levellog.getContent());
    }

    public void update(final Long id, final LevellogCreateRequest request) {
        final Levellog levellog = getLevellog(id);
        levellog.updateContent(request.getContent());
    }

    private Levellog getLevellog(final Long id) {
        return levellogRepository.findById(id)
                .orElseThrow();
    }

    public void delete(final Long id) {
        levellogRepository.deleteById(id);
    }
}
