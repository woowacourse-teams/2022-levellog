package com.woowacourse.levellog.member.domain;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGithubId(int githubId);

    List<Member> findAllByNicknameContains(String nickname);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByGithubId(int githubId);

    default Member getMember(final Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(DebugMessage.init()
                        .append("memberId", memberId)));
    }
}
