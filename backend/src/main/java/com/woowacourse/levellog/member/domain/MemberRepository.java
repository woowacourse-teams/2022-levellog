package com.woowacourse.levellog.member.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGithubId(int githubId);

    List<Member> findAllByNicknameContains(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    boolean existsByGithubId(int githubId);

    default Member getMember(final Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(DebugMessage.init()
                        .append("memberId", memberId)));
    }
}
