package com.woowacourse.levellog.member.application;

import com.woowacourse.levellog.authentication.dto.GithubProfileDto;
import com.woowacourse.levellog.member.domain.CrewNicknameTable;
import com.woowacourse.levellog.member.domain.CrewNicknameTableRepository;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.dto.MemberCreateDto;
import com.woowacourse.levellog.member.dto.MemberDto;
import com.woowacourse.levellog.member.dto.MembersDto;
import com.woowacourse.levellog.member.dto.NicknameUpdateDto;
import com.woowacourse.levellog.member.exception.MemberAlreadyExistException;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CrewNicknameTableRepository crewNicknameTableRepository;

    @Transactional
    public Long save(final MemberCreateDto request) {
        checkSameGithubId(request);

        final Member member = createMember(request);
        final Member savedMember = memberRepository.save(member);

        return savedMember.getId();
    }

    @Transactional
    public Long saveIfNotExist(final GithubProfileDto request, final int githubId) {
        // TODO githubId를 안받아도 될듯? GithubProfileDto에 이미 정보가 존재함. + save의 예외 발생을 이용하면 로직 간단해질듯
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

    public MembersDto searchByNickname(final String nickname) {
        final List<MemberDto> responses = memberRepository.findAllByNicknameContains(nickname)
                .stream()
                .map(MemberDto::from)
                .collect(Collectors.toList());

        return new MembersDto(responses);
    }

    @Transactional
    public void updateNickname(final NicknameUpdateDto request, final Long memberId) {
        final Member member = getById(memberId);
        member.updateNickname(request.getNickname());
    }

    private void checkSameGithubId(final MemberCreateDto request) {
        final boolean isExistSameGithubId = memberRepository.existsByGithubId(request.getGithubId());
        if (isExistSameGithubId) {
            throw new MemberAlreadyExistException("멤버 중복 [githubId : " + request.getGithubId() + "]");
        }
    }

    private Member createMember(final MemberCreateDto request) {
        final Member member = request.toEntity();
        final Optional<CrewNicknameTable> crewName = crewNicknameTableRepository.findByGithubNickname(
                request.getNickname());
        if (crewName.isPresent()) {
            member.updateNickname(crewName.get().getCrewNickname());
        }

        return member;
    }

    private Member getById(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private Member getByGithubId(final int githubId) {
        return memberRepository.findByGithubId(githubId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [githubId : " + githubId + "]"));
    }
}
