package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.dto.MemberCreateDto;
import com.woowacourse.levellog.dto.MemberResponse;
import com.woowacourse.levellog.dto.MembersResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayName("MemberService의")
class MemberServiceTest {

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
    @DisplayName("findAll 메서드는 모든 멤버를 조회한다.")
    void findAll() {
        // given
        memberRepository.save(new Member("로마", 1234, "image.png"));
        memberRepository.save(new Member("페퍼", 1245, "image2.png"));

        // when
        final MembersResponse membersResponse = memberService.findAll();

        // then
        assertThat(membersResponse.getMembers()).hasSize(2);
    }

    @Test
    @DisplayName("findMemberById 메서드는 Id로 멤버의 정보를 조회한다.")
    void findMemberById() {
        // given
        final Member roma = memberRepository.save(new Member("로마", 1234, "image.png"));

        // when
        final MemberResponse memberResponse = memberService.findMemberById(roma.getId());

        // then
        assertAll(
                () -> assertThat(memberResponse.getId()).isEqualTo(roma.getId()),
                () -> assertThat(memberResponse.getNickname()).isEqualTo("로마"),
                () -> assertThat(memberResponse.getProfileUrl()).isEqualTo("image.png")
        );
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

    @Test
    @DisplayName("updateProfileUrl 메서드는 Gtihub Id에 해당하는 멤버 정보를 업데이트한다.")
    void updateProfileUrl() {
        // given
        final Member savedMember = memberRepository.save(new Member("로마", 1234567, "profileUrl.image"));
        final Long id = savedMember.getId();
        final String newProfileUrl = "newProfile.image";

        // when
        memberService.updateProfileUrl(id, newProfileUrl);

        // then
        final Member updateMember = memberRepository.findById(id).orElseThrow();
        assertThat(updateMember.getProfileUrl()).isEqualTo(newProfileUrl);
    }
}
