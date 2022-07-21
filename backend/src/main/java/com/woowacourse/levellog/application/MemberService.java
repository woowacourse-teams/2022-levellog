package com.woowacourse.levellog.application;

import com.woowacourse.levellog.authentication.exception.MemberNotFoundException;
import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.dto.MemberCreateDto;
import com.woowacourse.levellog.dto.MemberResponse;
import com.woowacourse.levellog.dto.MembersResponse;
import com.woowacourse.levellog.dto.NicknameUpdateDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(final MemberCreateDto memberCreateDto) {
        final Member member = new Member(memberCreateDto.getNickname(), memberCreateDto.getGithubId(),
                memberCreateDto.getProfileUrl());
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public MemberResponse findMemberById(final Long memberId) {
        final Member member = getById(memberId);
        return MemberResponse.from(member);
    }

    public MembersResponse findAll() {
        final List<MemberResponse> responses = memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());

        return new MembersResponse(responses);
    }

    public MembersResponse findAllByNicknameContains(final String nickname) {
        final List<MemberResponse> memberResponses = memberRepository.findAllByNicknameContains(nickname).stream()
                .map(MemberResponse::from)
                .collect(Collectors.toList());
        return new MembersResponse(memberResponses);
    }

    public Optional<Member> findByGithubId(final int githubId) {
        return memberRepository.findByGithubId(githubId);
    }

    public void updateProfileUrl(final Long id, final String profileUrl) {
        final Member member = getById(id);
        member.updateProfileUrl(profileUrl);
    }

    public void updateNickname(final Long memberId, final NicknameUpdateDto nicknameUpdateDto) {
        final Member member = getById(memberId);
        member.updateNickname(nicknameUpdateDto.getNickname());
    }

    private Member getById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}
