package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.member.domain.Nickname;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.MembersDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.member.exception.MemberAlreadyExistException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MemberService의")
class MemberServiceTest extends ServiceTest {

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
        memberService.updateNickname(nicknameUpdateDto, id);

        // then
        final Member updateMember = memberRepository.findById(id)
                .orElseThrow();
        assertThat(updateMember.getNickname()).isEqualTo("알린");
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("사전에 깃허브 닉네임을 등록하지 않은 새로운 멤버를 저장한다.")
        void success() {
            // given
            final MemberCreateDto memberCreateDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");

            // when
            final Long id = memberService.save(memberCreateDto);

            // then
            assertThat(memberRepository.findById(id)).isPresent();
        }

        @Test
        @DisplayName("사전에 깃허브 닉네임에 대한 특수한 닉네임을 등록한 멤버가 저장될 때는 미리 저장한 닉네임으로 변경하여 멤버를 저장한다.")
        void successBySpecial() {
            // given
            nicknameRepository.save(new Nickname("깃허브로마", "우테코로마"));
            final MemberCreateDto memberCreateDto = new MemberCreateDto("깃허브로마", 12345678, "profileUrl.image");

            // when
            final Long id = memberService.save(memberCreateDto);

            // then
            assertAll(
                    () -> assertThat(memberRepository.findById(id)).isPresent(),
                    () -> assertThat(memberRepository.findById(id).get().getNickname()).isEqualTo("우테코로마")
            );
        }

        @Test
        @DisplayName("동일한 깃허브로 가입한 멤버가 존재하면 예외를 던진다.")
        void memberAlreadyExist_exception() {
            // given
            final MemberCreateDto beforeSavedDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");
            memberService.save(beforeSavedDto);

            final MemberCreateDto newSaveDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");

            // when & then
            assertThatThrownBy(() -> memberService.save(newSaveDto))
                    .isInstanceOf(MemberAlreadyExistException.class)
                    .hasMessageContainingAll("멤버 중복", String.valueOf(newSaveDto.getGithubId()));
        }
    }

    @Nested
    @DisplayName("findMemberById 메서드는")
    class FindMemberById {

        @Test
        @DisplayName("Id로 멤버의 정보를 조회한다.")
        void success() {
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
        @DisplayName("존재하지 않는 Id로 요청을 보낼 경우 예외를 던진다.")
        void memberNotFound_exception() {
            // when & then
            assertThatThrownBy(() -> memberService.findMemberById(1000L))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않음", "1000");
        }

    }

    @Nested
    @DisplayName("saveIfNotExist 메서드는")
    class SaveIfNotExist {

        @Test
        @DisplayName("깃허브 아이디로 저장된 멤버가 있으면 가입된 멤버의 ID를 반환한다.")
        void ifExist_returnSavedId() {
            // given
            final Member savedMember = memberRepository.save(new Member("로마", 123456, "profileUrl.image"));

            // when
            final Long id = memberService.saveIfNotExist(new GithubProfileDto("123456", "test", "test.image"), 123456);

            // then
            assertThat(savedMember.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("깃허브 아이디로 저장된 멤버가 없으면 새 멤버를 저장하고 멤버의 ID를 반환한다.")
        void ifNotExist_saveAndReturnSavedId() {
            // given
            final Member savedMember = memberRepository.save(new Member("로마", 123456, "profileUrl.image"));

            // when
            final Long id = memberService.saveIfNotExist(new GithubProfileDto("100", "test", "test.image"), 100);

            // then
            assertThat(id).isEqualTo(savedMember.getId() + 1);
        }
    }
}
