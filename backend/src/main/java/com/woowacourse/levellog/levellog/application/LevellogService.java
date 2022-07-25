package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogRequest;
import com.woowacourse.levellog.levellog.dto.LevellogResponse;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.levellog.exception.UnauthorizedException;
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
@Transactional
@RequiredArgsConstructor
public class LevellogService {

    private final LevellogRepository levellogRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public Long save(final Long authorId, final Long groupId, final LevellogRequest request) {
        levellogRepository.findByAuthorIdAndTeamId(authorId, groupId)
                .ifPresent(it -> {
                    throw new LevellogAlreadyExistException();
                });

        final Team team = getTeam(groupId);
        final Member author = getMember(authorId);
        final Levellog levellog = new Levellog(author, team, request.getContent());

        final Levellog savedLevellog = levellogRepository.save(levellog);
        return savedLevellog.getId();
    }

    @Transactional(readOnly = true)
    public LevellogResponse findById(final Long id) {
        final Levellog levellog = getById(id);
        return new LevellogResponse(levellog.getContent());
    }

    public void update(final Long levellogId, final Long memberId, final LevellogRequest request) {
        final Levellog levellog = getById(levellogId);
        validateAuthor(levellog, memberId, "레벨로그를 수정할 권한이 없습니다.");
        levellog.updateContent(request.getContent());
    }

    public void deleteById(final Long levellogId, final Long memberId) {
        final Levellog levellog = getById(levellogId);
        validateAuthor(levellog, memberId, "레벨로그를 삭제할 권한이 없습니다.");
        levellogRepository.deleteById(levellogId);
    }

    private void validateAuthor(final Levellog levellog, final Long memberId, final String message) {
        if (!levellog.isAuthorId(memberId)) {
            throw new UnauthorizedException(message);
        }
    }

    private Levellog getById(final Long id) {
        return levellogRepository.findById(id).orElseThrow(LevellogNotFoundException::new);
    }

    private Team getTeam(final Long groupId) {
        return teamRepository.findById(groupId).orElseThrow(TeamNotFoundException::new);
    }

    private Member getMember(final Long memberId) {
        return memberRepository
                .findById(memberId).orElseThrow(MemberNotFoundException::new);
    }
}
