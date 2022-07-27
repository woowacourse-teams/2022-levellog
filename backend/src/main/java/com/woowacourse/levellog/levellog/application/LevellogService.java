package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LevellogService {

    private final LevellogRepository levellogRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(final LevellogWriteDto request, final Long authorId, final Long teamId) {
        final Team team = getTeam(teamId);
        final Member author = getMember(authorId);
        validateLevelogExistence(authorId, teamId);

        final Levellog levellog = request.toLevellog(author, team);
        final Levellog savedLevellog = levellogRepository.save(levellog);

        return savedLevellog.getId();
    }

    public LevellogDto findById(final Long levellogId) {
        final Levellog levellog = getById(levellogId);

        return LevellogDto.from(levellog);
    }

    @Transactional
    public void update(final LevellogWriteDto request, final Long levellogId, final Long memberId) {
        final Levellog levellog = getById(levellogId);
        final Member member = getMember(memberId);

        levellog.updateContent(member, request.getContent());
    }

    @Transactional
    public void deleteById(final Long levellogId, final Long memberId) {
        final Levellog levellog = getById(levellogId);
        final Member member = getMember(memberId);
        validateAuthor(member, levellog);

        levellogRepository.deleteById(levellogId);
    }

    private Levellog getById(final Long levellogId) {
        return levellogRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException("레벨로그가 존재하지 않습니다. id : " + levellogId));
    }

    private Team getTeam(final Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다. id : " + teamId));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않습니다. id : " + memberId));
    }

    private void validateLevelogExistence(final Long authorId, final Long teamId) {
        final boolean isExists = levellogRepository.existsByAuthorIdAndTeamId(authorId, teamId);
        if (isExists) {
            throw new LevellogAlreadyExistException("레벨로그를 이미 작성하였습니다. authorId : " + authorId + " teamId : " + teamId);
        }
    }

    private void validateAuthor(final Member member, final Levellog levellog) {
        final boolean isNotAuthor = !levellog.isAuthor(member);
        if (isNotAuthor) {
            throw new UnauthorizedException("레벨로그를 삭제할 권한이 없습니다. memberId : " + member.getId()
                    + " levellogId: " + levellog.getId());
        }
    }
}
