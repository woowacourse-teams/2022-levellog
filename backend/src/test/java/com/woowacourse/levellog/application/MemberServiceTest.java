package com.woowacourse.levellog.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.NicknameMapping;
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
        saveMember("roma");
        saveMember("pepper");
        saveMember("rick");
        saveMember("eve");
        saveMember("kyul");
        saveMember("harry");

        final Member alien = saveMember("alien");

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
        final Long memberId = saveMember("로마").getId();
        final NicknameUpdateDto nicknameUpdateDto = new NicknameUpdateDto("알린");

        // when
        memberService.updateNickname(nicknameUpdateDto, memberId);

        // then
        final Member updateMember = memberRepository.findById(memberId)
                .orElseThrow();
        assertThat(updateMember.getNickname()).isEqualTo("알린");
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("사전에 깃허브 닉네임을 등록하지 않은 새로운 멤버를 저장한다.")
        void save_notRegistered_success() {
            // given
            final MemberCreateDto memberCreateDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");

            // when
            final Long id = memberService.save(memberCreateDto);

            // then
            assertAll(
                    () -> assertThat(memberRepository.findById(id)).isPresent(),
                    () -> assertThat(memberRepository.findById(id).get().getNickname()).isEqualTo("로마")
            );
        }

        @Test
        @DisplayName("사전에 깃허브 닉네임에 대한 특수한 닉네임을 등록한 멤버가 저장될 때는 미리 저장한 닉네임으로 변경하여 멤버를 저장한다.")
        void save_nicknameMapping_success() {
            // given
            nicknameMappingRepository.save(new NicknameMapping("깃허브로마", "우테코로마"));
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
        void save_memberAlreadyExist_exception() {
            // given
            final MemberCreateDto beforeSavedDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");
            memberService.save(beforeSavedDto);

            final MemberCreateDto newSaveDto = new MemberCreateDto("로마", 12345678, "profileUrl.image");

            // when & then
            assertThatThrownBy(() -> memberService.save(newSaveDto))
                    .isInstanceOf(MemberAlreadyExistException.class)
                    .hasMessageContainingAll("멤버가 이미 존재합니다.", String.valueOf(newSaveDto.getGithubId()));
        }
    }

    @Nested
    @DisplayName("findMemberById 메서드는")
    class FindMemberById {

        @Test
        @DisplayName("Id로 멤버의 정보를 조회한다.")
        void success() {
            // given
            final Member roma = saveMember("로마");

            // when
            final MemberDto memberDto = memberService.findMemberById(roma.getId());

            // then
            assertAll(
                    () -> assertThat(memberDto.getId()).isEqualTo(roma.getId()),
                    () -> assertThat(memberDto.getNickname()).isEqualTo("로마")
            );
        }

        @Test
        @DisplayName("존재하지 않는 Id로 요청을 보낼 경우 예외를 던진다.")
        void findMemberById_memberNotFound_exception() {
            // when & then
            assertThatThrownBy(() -> memberService.findMemberById(1000L))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessageContainingAll("멤버가 존재하지 않습니다.", "1000");
        }

    }

    @Nested
    @DisplayName("saveIfNotExist 메서드는")
    class SaveIfNotExist {

        @Test
        @DisplayName("깃허브 아이디로 저장된 멤버가 있으면 가입된 멤버의 ID를 반환한다.")
        void saveIfNotExist_ifExist_success() {
            // given
            final Member savedMember = saveMember("로마");
            final Integer githubId = savedMember.getGithubId();

            final GithubProfileDto githubProfileDto = new GithubProfileDto(githubId.toString(), "test", "test.image");

            // when
            final Long id = memberService.saveIfNotExist(githubProfileDto, githubId);

            // then
            assertThat(savedMember.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("깃허브 아이디로 저장된 멤버가 없으면 새 멤버를 저장하고 멤버의 ID를 반환한다.")
        void saveIfNotExist_ifNotExist_success() {
            // given
            final Member savedMember = saveMember("로마");
            final int githubId = savedMember.getGithubId() + 999;

            final GithubProfileDto githubProfileDto = new GithubProfileDto(Integer.toString(githubId), "test",
                    "test.image");

            // when
            final Long id = memberService.saveIfNotExist(githubProfileDto, githubId);

            // then
            assertThat(id).isEqualTo(savedMember.getId() + 1);
        }
    }
}
