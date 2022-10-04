package com.woowacourse.levellog.levellog.application;

import com.woowacourse.levellog.common.support.DebugMessage;
import com.woowacourse.levellog.levellog.domain.Levellog;
import com.woowacourse.levellog.levellog.domain.LevellogRepository;
import com.woowacourse.levellog.levellog.dto.request.LevellogWriteRequest;
import com.woowacourse.levellog.levellog.dto.response.LevellogResponse;
import com.woowacourse.levellog.levellog.dto.response.LevellogListResponses;
import com.woowacourse.levellog.levellog.dto.response.LevellogWithIdResponse;
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
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TimeStandard timeStandard;

    @Transactional
    public Long save(final LevellogWriteRequest request, final Long authorId, final Long teamId) {
        final Team team = teamRepository.getTeam(teamId);
        final Member author = memberRepository.getMember(authorId);
        validateLevellogExistence(authorId, teamId);
        team.validateReady(timeStandard.now());

        final Levellog savedLevellog = levellogRepository.save(request.toLevellog(author, team));

        return savedLevellog.getId();
    }

    public LevellogResponse findById(final Long levellogId) {
        final Levellog levellog = getById(levellogId);

        return LevellogResponse.from(levellog);
    }

    public LevellogListResponses findAllByAuthorId(final Long authorId) {
        final Member author = memberRepository.getMember(authorId);

        final List<Levellog> levellogs = levellogRepository.findAllByAuthor(author);
        final List<LevellogWithIdResponse> levellogWithIdResponses = levellogs.stream()
                .map(LevellogWithIdResponse::from)
                .collect(Collectors.toList());

        return new LevellogListResponses(levellogWithIdResponses);
    }

    @Transactional
    public void update(final LevellogWriteRequest request, final Long levellogId, final Long memberId) {
        final Levellog levellog = getById(levellogId);
        final Member member = memberRepository.getMember(memberId);
        levellog.getTeam().validateReady(timeStandard.now());

        levellog.updateContent(member, request.getContent());
    }

    private Levellog getById(final Long levellogId) {
        return levellogRepository.findLevellogAndMemberByLevelogId(levellogId)
                .orElseThrow(() -> new LevellogNotFoundException(DebugMessage.init()
                        .append("levellogId", levellogId)));
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
