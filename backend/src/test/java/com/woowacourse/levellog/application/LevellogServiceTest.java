package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Levellog;
import com.woowacourse.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.dto.LevellogCreateRequest;
import com.woowacourse.levellog.dto.LevellogResponse;
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

    @Test
    @DisplayName("findById 메서드는 id에 해당하는 레벨로그를 조회한다.")
    void findById() {
        // given
        final String content = "content";
        final Levellog levellog = levellogRepository.save(new Levellog(content));

        // when
        final LevellogResponse response = levellogService.findById(levellog.getId());

        // then
        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("update 메서드는 id에 해당하는 레벨로그를 변경한다.")
    void update() {
        // given
        final Levellog levellog = levellogRepository.save(new Levellog("original content"));
        final LevellogCreateRequest request = new LevellogCreateRequest("update content");

        // when
        levellogService.update(levellog.getId(), request);

        // then
        final Levellog actual = levellogRepository.findById(levellog.getId())
                .orElseThrow();
        assertThat(actual.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("deleteById 메서드는 id에 해당하는 레벨로그를 삭제한다.")
    void deleteById() {
        // given
        final Levellog levellog = levellogRepository.save(new Levellog("original content"));

        // when
        levellogService.deleteById(levellog.getId());

        // then
        final Optional<Levellog> actual = levellogRepository.findById(levellog.getId());
        assertThat(actual).isEmpty();
    }
}
