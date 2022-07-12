package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.dto.LevellogCreateRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public LevellogResponse find(final Long id) {
        final Levellog levellog = levellogRepository.findById(id)
                .orElseThrow();
        return new LevellogResponse(levellog.getContent());
    }
}
