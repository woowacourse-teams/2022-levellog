package com.woowacourse.levellog.member.application;

import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.MembersDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(final MemberCreateDto request) {
        final Member member = new Member(request.getNickname(), request.getGithubId(), request.getProfileUrl());
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Transactional
    public Long saveIfNotExist(final GithubProfileDto request, final int githubId) {
        final boolean isExist = memberRepository.existsByGithubId(githubId);
        if (isExist) {
            return getByGithubId(githubId).getId();
        }

        return save(new MemberCreateDto(request.getNickname(), githubId, request.getProfileUrl()));
    }

    public MemberDto findMemberById(final Long memberId) {
        final Member member = getById(memberId);

        return MemberDto.from(member);
    }

    public MembersDto findAll() {
        final List<MemberDto> responses = memberRepository.findAll()
                .stream()
                .map(MemberDto::from)
                .collect(Collectors.toList());

        return new MembersDto(responses);
    }

    public MembersDto searchByNickname(final String nickname) {
        final List<MemberDto> responses = memberRepository.findAllByNicknameContains(nickname)
                .stream()
                .map(MemberDto::from)
                .collect(Collectors.toList());

        return new MembersDto(responses);
    }

    @Transactional
    public void updateNickname(final Long memberId, final NicknameUpdateDto request) {
        final Member member = getById(memberId);
        member.updateNickname(request.getNickname());
    }

    private Member getById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member getByGithubId(final int githubId) {
        return memberRepository.findByGithubId(githubId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
