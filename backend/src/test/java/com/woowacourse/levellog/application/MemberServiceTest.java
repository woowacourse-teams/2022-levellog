package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.dto.MemberCreateDto;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
@DisplayName("MemberService의")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save 메서드는 새로운 멤버를 저장한다.")
    void save() {
        // given
        final MemberCreateDto memberCreateDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");

        // when
        final Long id = memberService.save(memberCreateDto);

        // then
        assertThat(memberRepository.findById(id)).isPresent();
    }

    @Test
    @DisplayName("findByGithubId 메서드는 Gtihub Id에 해당하는 멤버를 찾는다.")
    void findByGithubId() {
        // given
        final int githubId = 12345678;
        memberRepository.save(new Member("로마", githubId, "profileUrl.image"));

        // when
        final Optional<Member> findMember = memberService.findByGithubId(githubId);

        // then
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getGithubId()).isEqualTo(githubId);
    }
}
