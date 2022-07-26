package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.member.application.MemberService;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.MembersDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
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
        final MembersDto membersDto = memberService.findAll();

        // then
        assertThat(membersDto.getMembers()).hasSize(2);
    }

    @Test
    @DisplayName("findMemberById 메서드는 Id로 멤버의 정보를 조회한다.")
    void findMemberById() {
        // given
        final Member roma = memberRepository.save(new Member("로마", 1234, "image.png"));

        // when
        final MemberDto memberDto = memberService.findMemberById(roma.getId());

        // then
        assertAll(
                () -> assertThat(memberDto.getId()).isEqualTo(roma.getId()),
                () -> assertThat(memberDto.getNickname()).isEqualTo("로마"),
                () -> assertThat(memberDto.getProfileUrl()).isEqualTo("image.png")
        );
    }

    @Test
    @DisplayName("findAllByNicknameContains 메서드는 입력한 문자열이 포함된 nickname을 가진 멤버를 모두 조회한다.")
    void findAllByNicknameContains() {
        // given
        final Member roma = memberRepository.save(new Member("roma", 10, "roma.img"));
        final Member pepper = memberRepository.save(new Member("pepper", 20, "pepper.img"));
        final Member alien = memberRepository.save(new Member("alien", 30, "alien.img"));
        final Member rick = memberRepository.save(new Member("rick", 40, "rick.img"));
        final Member eve = memberRepository.save(new Member("eve", 50, "eve.img"));
        final Member kyul = memberRepository.save(new Member("kyul", 60, "kyul.img"));
        final Member harry = memberRepository.save(new Member("harry", 70, "harry.img"));

        // when
        final MembersDto members = memberService.searchByNickname("ali");

        // then
        assertAll(
                () -> assertThat(members.getMembers()).hasSize(1),
                () -> assertThat(members.getMembers().get(0).getId()).isEqualTo(alien.getId())
        );
    }

    @Test
    @DisplayName("updateNickname 메서드는 닉네임을 업데이트한다.")
    void updateNickname() {
        // given
        final Member savedMember = memberRepository.save(new Member("로마", 1234567, "profileUrl.image"));
        final Long id = savedMember.getId();
        final NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto("알린");

        // when
        memberService.updateNickname(id, nicknameUpdateDto);

        // then
        final Member updateMember = memberRepository.findById(id).orElseThrow();
        assertThat(updateMember.getNickname()).isEqualTo("알린");
    }
}
