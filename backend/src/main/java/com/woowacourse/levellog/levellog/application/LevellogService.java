package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.common.exception.UnauthorizedException;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.levellog.dto.LevellogWithIdDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.levellog.dto.LevellogsDto;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.member.exception.MemberNotFoundException;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
import com.woowacourse.levellog.team.exception.InterviewTimeException;
import com.woowacourse.levellog.team.exception.TeamNotFoundException;
import com.woowacourse.levellog.team.support.TimeStandard;
import java.util.List;
import java.util.stream.Collectors;
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
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final LevellogWriteDto request, final Long authorId, final Long teamId) {
        final Team team = getTeam(teamId);
        final Member author = getMember(authorId);
        validateLevellogExistence(authorId, teamId);
        // TODO TEAM 도메인에 해당 로직 삽입 예정 ( 다른 팀원들 어떻게 하는지 보고 해결하자 )
        if (team.isAfterStartTime(timeStandard.now())) {
            throw new InterviewTimeException("인터뷰 시작 전에만 레벨로그 작성이 가능합니다.", "[teamId : " + teamId + "]");
        }

        final Levellog levellog = request.toLevellog(author, team);
        final Levellog savedLevellog = levellogRepository.save(levellog);

        return savedLevellog.getId();
    }

    public LevellogDto findById(final Long levellogId) {
        final Levellog levellog = getById(levellogId);

        return LevellogDto.from(levellog);
    }

    public LevellogsDto findAllByAuthorId(final Long authorId) {
        final Member author = getMember(authorId);

        final List<Levellog> levellogs = levellogRepository.findAllByAuthor(author);
        final List<LevellogWithIdDto> levellogWithIdDtos = levellogs.stream()
                .map(LevellogWithIdDto::from)
                .collect(Collectors.toList());

        return new LevellogsDto(levellogWithIdDtos);
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
                .orElseThrow(() -> new LevellogNotFoundException("레벨로그가 존재하지 않습니다. levellogId : " + levellogId));
    }

    private Team getTeam(final Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다. teamId : " + teamId, "팀이 존재하지 않습니다."));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버가 존재하지 않음 [memberId : " + memberId + "]"));
    }

    private void validateLevellogExistence(final Long authorId, final Long teamId) {
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
