package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogQueryRepository;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.LevellogDto;
import com.woowacourse.levellog.levellog.dto.LevellogWithIdDto;
import com.woowacourse.levellog.levellog.dto.LevellogWriteDto;
import com.woowacourse.levellog.levellog.dto.LevellogsDto;
import com.woowacourse.levellog.levellog.exception.LevellogAlreadyExistException;
import com.woowacourse.levellog.levellog.exception.LevellogNotFoundException;
import com.woowacourse.levellog.member.domain.Member;
import com.woowacourse.levellog.member.domain.MemberRepository;
import com.woowacourse.levellog.team.domain.Team;
import com.woowacourse.levellog.team.domain.TeamRepository;
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
    private final LevellogQueryRepository levellogQueryRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final LevellogWriteDto request, final Long authorId, final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        final Member author = memberRepository.getMember(authorId);
        validateLevellogExistence(authorId, teamId);
        team.validateReady(timeStandard.now());

        final Levellog savedLevellog = levellogRepository.save(request.toLevellog(author, team));

        return savedLevellog.getId();
    }

    public LevellogDto findById(final Long levellogId) {
        return levellogQueryRepository.findById(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
    }

    public LevellogsDto findAllByAuthorId(final Long authorId) {
        final Member author = memberRepository.getMember(authorId);

        final List<Levellog> levellogs = levellogRepository.findAllByAuthor(author);
        final List<LevellogWithIdDto> levellogWithIdDtos = levellogs.stream()
                .map(LevellogWithIdDto::from)
                .collect(Collectors.toList());

        return new LevellogsDto(levellogWithIdDtos);
    }

    @Transactional
    public void update(final LevellogWriteDto request, final Long levellogId, final Long memberId) {
        final Levellog levellog = levellogRepository.getLevellog(levellogId);
        final Member member = memberRepository.getMember(memberId);
        levellog.getTeam().validateReady(timeStandard.now());

        levellog.updateContent(member, request.getContent());
    }

    private void validateLevellogExistence(final Long authorId, final Long teamId) {
        final boolean isExists = levellogRepository.existsByAuthorIdAndTeamId(authorId, teamId);
        if (isExists) {
            throw new LevellogAlreadyExistException(DebugMessage.init()
                    .append("authorId", authorId)
                    .append("teamId", teamId));
        }
    }
}
