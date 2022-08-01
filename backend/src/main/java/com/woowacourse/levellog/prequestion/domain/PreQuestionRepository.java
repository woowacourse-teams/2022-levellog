package com.woowacourse.levellog.prequestion.domain;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreQuestionRepository extends JpaRepository<PreQuestion, Long> {

    Optional<PreQuestion> findByIdAndFrom(Long id, Member from);

    boolean existsByLevellogAndFrom(Levellog levellog, Member member);
}
