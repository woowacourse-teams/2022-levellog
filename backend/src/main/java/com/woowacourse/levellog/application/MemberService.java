package com.woowacourse.levellog.application;

import com.woowacourse.levellog.domain.Member;
import com.woowacourse.levellog.domain.MemberRepository;
import com.woowacourse.levellog.dto.MemberCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(final MemberCreateDto memberCreateDto) {
        final Member member = new Member(memberCreateDto.getNickname(), memberCreateDto.getGithubId(),
                memberCreateDto.getProfileUrl());
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
