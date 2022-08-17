package com.woowacourse.levellog.member.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByGithubId(int githubId);

    @Query("SELECT p FROM Member p WHERE p.nickname LIKE %:nickname% OR p.githubNickname LIKE %:nickname%")
    List<Member> findAllByNicknameOrGithubNicknameContains(String nickname);

    boolean existsByGithubId(int githubId);
}
