package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.dto.LevellogCreateRequest;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Transactional
@DisplayName("LevellogService의")
class LevellogServiceTest {

    @Autowired
    private LevellogService levellogService;

    @Autowired
    private LevellogRepository levellogRepository;

    @Test
    @DisplayName("save 메서드는 레벨로그를 저장한다.")
    void save() {
        // given
        final LevellogCreateRequest request = new LevellogCreateRequest("굳굳");

        // when
        final Long id = levellogService.save(request);

        // then
        final Optional<Levellog> levellog = levellogRepository.findById(id);
        assertThat(levellog).isPresent();
    }
}
